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
        List<ProcesarDatosService.Metrica> metrics = new ArrayList<>();

        // Verificar si las métricas están vacías y agregar valores por defecto
        for (AntenaInDTO antena : antenas) {
            if (nombrePod.equals("")) {
                nombrePod = antena.getPod();
            }
            List<String> metricasAntena = antena.getMetrics();
            metricasAntena.replaceAll(metrica -> metrica == null || metrica.isEmpty() ? "N/A" : metrica);
            for (int i = 0; i < metricasAntena.size(); i++) {
                String metricaAntena = metricasAntena.get(i);
                if (!metricaAntena.equals("N/A")) {
                    if (metrics.size() <= i) {
                        metrics.add(new ProcesarDatosService.Metrica(metricaAntena, antena.getDistance()));
                    } else if (metrics.get(i).getValor().equals("N/A")) {
                        metrics.set(i, new ProcesarDatosService.Metrica(metricaAntena, antena.getDistance()));
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
        if (metricasPorDefecto == null) {
            // Si no se encontró una métrica válida en la última antena, entonces no se pueden calcular las métricas.
            // Devolver un mensaje de error.
            return new ResponseEntity<>("No se puede calcular la métrica, falta de información", HttpStatus.BAD_REQUEST);
        }

        response.put("metrics", String.join(",", metricasPorDefecto));
        antenaService.saveAntenas(antenas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
