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
public class AntenaPosicionService {

    private final ProcesarDatosService metricProcessor;

    public AntenaPosicionService(ProcesarDatosService metricProcessor) {
        this.metricProcessor = metricProcessor;
    }

    public ResponseEntity<?> calculateAntenaPosition(List<AntenaInDTO> antenas) {

        String podName = "";
        List<ProcesarDatosService.Metric> metrics = new ArrayList<>();

        // Verificar si las métricas están vacías y agregar valores por defecto
        for (AntenaInDTO antena : antenas) {
            if (podName.equals("")) {
                podName = antena.getPod();
            }
            List<String> antenaMetrics = antena.getMetrics();
            antenaMetrics.replaceAll(metric -> metric == null || metric.isEmpty() ? "N/A" : metric);
            for (int i = 0; i < antenaMetrics.size(); i++) {
                String antenaMetric = antenaMetrics.get(i);
                if (!antenaMetric.equals("N/A")) {
                    if (metrics.size() <= i) {
                        metrics.add(new ProcesarDatosService.Metric(antenaMetric, antena.getDistance()));
                    } else if (metrics.get(i).getValue().equals("N/A")) {
                        metrics.set(i, new ProcesarDatosService.Metric(antenaMetric, antena.getDistance()));
                    }
                }
            }
        }

        // Comprobar si no hay antenas para calcular la posición
        if (antenas.isEmpty()) {
            return new ResponseEntity<>("No hay antenas para calcular la posición", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> position = new HashMap<>();
        double totalDistance = antenas.stream().mapToDouble(AntenaInDTO::getDistance).sum();
        position.put("distance", totalDistance);
        position.put("distance_copy", totalDistance / antenas.size());

        Map<String, Object> response = new HashMap<>();
        response.put("pod", podName);
        response.put("position", position);

        List<String> defaultMetrics = metricProcessor.getDefaultMetrics(metrics, antenas);
        if (defaultMetrics == null) {
            // Si no se encontró una métrica válida en la última antena, entonces no se pueden calcular las métricas.
            // Devolver un mensaje de error.
            return new ResponseEntity<>("No se puede calcular la métrica debido a la falta de información", HttpStatus.BAD_REQUEST);
        }

        response.put("metrics", String.join(",", defaultMetrics));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
