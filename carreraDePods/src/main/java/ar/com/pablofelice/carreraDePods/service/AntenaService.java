package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.persistence.repository.AntenaRepository;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import ar.com.pablofelice.carreraDePods.utils.CrearAntena;
import ar.com.pablofelice.carreraDePods.utils.DistanciaAntena;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntenaService {

    private final AntenaRepository antenaRepository;
    private final IMapper<AntenaInDTO, Antena> antenaMapper;

    @Autowired
    public AntenaService(AntenaRepository antenaRepository, IMapper<AntenaInDTO, Antena> antenaMapper) {
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

        // Recorre la lista recibida desde el controlador, si el nombre estoa vacio lo asigna
        for (AntenaInDTO antena : antenas) {
            if (nombrePod.equals("")) {
                nombrePod = antena.getPod();
            }
            //Completar Métricas Vacías
            for (int i = 0; i < metricasFinales.size(); i++) {
                if (metricasFinales.get(i) == null || metricasFinales.get(i).isEmpty()) {
                    for (AntenaInDTO otraAntena : antenas) {
                        if (otraAntena.getMetrics().get(i) != null && !otraAntena.getMetrics().get(i).isEmpty()) {
                            System.out.println("------------> Cambié el valor numero: " + i + " Que era: " + antena.getMetrics().get(i) + " Cadena analizada" + antena.getMetrics().toString());
                            System.out.println("------------> Por el VALOR numero: " + i + " Que ES: " + otraAntena.getMetrics().get(i) + " LLeno de: " + otraAntena.getMetrics().toString());
                            metricasFinales.set(i, otraAntena.getMetrics().get(i));
                            break;
                        }
                    }

                }
            }
        }
        System.out.println("************************** PROBANDO COORDENADAS ************************************");
        List<CrearAntena> ListaAntenas = new ArrayList<CrearAntena>();
        ListaAntenas.add(new CrearAntena("Antena1", -500, -200));
        ListaAntenas.add(new CrearAntena("Antena2", 100, -100));
        ListaAntenas.add(new CrearAntena("Antena3", 500, 100));
        List<DistanciaAntena> listaDistancias = new ArrayList<DistanciaAntena>();
        listaDistancias.add(new DistanciaAntena("Antena1", 210.0f));
        listaDistancias.add(new DistanciaAntena("Antena2", 225.5f));
        listaDistancias.add(new DistanciaAntena("Antena3", 252.7f));
        MessageLocationService coor = new MessageLocationService(ListaAntenas);
        float[] result=coor.getLocation(listaDistancias);
        System.out.println("RESULTADO::::::::::::::");
        System.out.println(Arrays.toString(result));
        System.out.println("***************************        FIN                 *****************************");
        //Si MetricaFinal quedó  Vacía devuelve Error 404
        for (int i = 0; i < metricasFinales.size(); i++) {
            if (metricasFinales.get(i) == null || metricasFinales.get(i).isEmpty()) {
                return new ResponseEntity<>("RESPONSE CODE: 404", HttpStatus.NOT_FOUND);
            }
        }

        Map<String, Object> posicion = new HashMap<>();
        double distanciaTotal = antenas.stream().mapToDouble(AntenaInDTO::getDistance).sum();
        posicion.put("distancia", distanciaTotal);
        posicion.put("distancia_media", distanciaTotal / antenas.size());

        Map<String, Object> response = new HashMap<>();
        response.put("pod", nombrePod);
        response.put("position", posicion);

        response.put("metrics", metricasFinales);
        this.saveAntenas(antenas);
        System.out.println("Ver Response = " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
