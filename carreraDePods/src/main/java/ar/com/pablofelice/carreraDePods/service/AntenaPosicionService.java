package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
public class AntenaPosicionService {private final ProcesarDatosService procesarDatosService;

public AntenaPosicionService(ProcesarDatosService procesarDatosService) {
    this.procesarDatosService = procesarDatosService;
}

public ResponseEntity<?> calcularPosicionAntena(List<AntenaInDTO> antenas) {

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
    return new ResponseEntity<>(response, HttpStatus.OK);
}
}
