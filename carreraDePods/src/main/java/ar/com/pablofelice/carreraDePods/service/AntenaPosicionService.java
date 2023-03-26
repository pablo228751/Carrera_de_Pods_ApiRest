package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.persistence.repository.AntenaRepository;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntenaPosicionService {

    private final ProcesarDatosService procesarDatosService;
    private final AntenaRepository antenaRepository;
    private final IMapper<AntenaInDTO, Antena> antenaMapper;
    //private static final Logger logger = LoggerFactory.getLogger(AntenaService.class);
    @Autowired
    private AntenaService antenaService;

    @Autowired
    public AntenaPosicionService(ProcesarDatosService procesarDatosService, AntenaRepository antenaRepository, IMapper<AntenaInDTO, Antena> antenaMapper) {
        this.procesarDatosService = procesarDatosService;
        this.antenaRepository = antenaRepository;
        this.antenaMapper = antenaMapper;
    }

    // método para guardar los datos de las antenas
    public void saveAntenas(List<AntenaInDTO> antenaInDTOS) {

        //logger.info("antenaInDTOS: " + antenaInDTOS);
        List<Antena> antenas = antenaInDTOS.stream()
                .map(antenaMapper::mapToEntity)
                .collect(Collectors.toList());
        //logger.info("antenas: " + antenas);
        antenaRepository.saveAll(antenas);
    }

    public ResponseEntity<?> calcularPosicionAntena(List<AntenaInDTO> antenas) {
        //logger.info("Paso1 ");
        String nombrePod = "";
        List<String> metricasFinales = antenas.get(0).getMetrics();
        List<ProcesarDatosService.Metrica> metrics = new ArrayList<>();        
        

        // Recorre la lista recibida desde el controlador, si el nombre estoa vacio lo asigna (solo se aplicaria al primer elemento)
        for (AntenaInDTO antena : antenas) {
            if (nombrePod.equals("")) {
                nombrePod = antena.getPod();
            }
            //Rellenar Métricas
            for (int i = 0; i < metricasFinales.size(); i++) {
                if (metricasFinales.get(i) == null || metricasFinales.get(i).isEmpty()) {
                    for (AntenaInDTO otraAntena : antenas) {
                        if (otraAntena.getMetrics().get(i) != null && !otraAntena.getMetrics().get(i).isEmpty()) {
                            System.out.println("------------> Cambié el valor numero: "+ i + " Que era: "+antena.getMetrics().get(i)+ " Cadena analizada"+ antena.getMetrics().toString());
                            System.out.println("------------> Por el VALOR numero: "+ i + " Que ES: "+otraAntena.getMetrics().get(i)+" LLeno de: "+ otraAntena.getMetrics().toString());                            
                            metricasFinales.set(i, otraAntena.getMetrics().get(i));
                            break;
                        }
                    }    

                }
                System.out.println("MetricasFinales va asi: "+metricasFinales);

                if (!metricasFinales.get(i).equals("N/A")) {
                    if (metrics.size() <= i) {
                        metrics.add(new ProcesarDatosService.Metrica(metricasFinales.get(i), antena.getDistance()));
                    } else if (metrics.get(i).getValor().equals("N/A")) {
                        metrics.set(i, new ProcesarDatosService.Metrica(metricasFinales.get(i), antena.getDistance()));
                    }
                }
            }
        }

        // Comprobar si no hay antenas para calcular la posición
        if (antenas.isEmpty()) {
            return new ResponseEntity<>("No se encontraron antenas para calcular la posición", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> posicion = new HashMap<>();
        double distanciaTotal = antenas.stream().mapToDouble(AntenaInDTO::getDistance).sum();
        posicion.put("distancia", distanciaTotal);
        posicion.put("distancia_media", distanciaTotal / antenas.size());

        Map<String, Object> response = new HashMap<>();
        response.put("pod", nombrePod);
        response.put("position", posicion);

        List<String> metricasPorDefecto = procesarDatosService.getMetricasPorDefecto(metrics, antenas);
        System.out.println("Métricas por defecto:");
        System.out.println(metricasPorDefecto);
        if (metricasPorDefecto == null) {
            // Si no se encontró una métrica válida en la última antena, entonces no se pueden calcular las métricas.
            // Devolver un mensaje de error.
            return new ResponseEntity<>("No se puede calcular la métrica, falta de información", HttpStatus.BAD_REQUEST);
        }

        response.put("metrics", metricasPorDefecto);
        antenaService.saveAntenas(antenas);
        System.out.println("Ver Response = " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
