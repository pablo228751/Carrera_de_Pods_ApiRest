package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
public class AntenaPosicionService {

    public AntenaPosicionService() {
    }

    public ResponseEntity<?> calculateAntenaPosition(List<AntenaInDTO> antenas) {

        double totalDistance = 0.0;
        String podName = "";
        List<Metric> metrics = new ArrayList<>();
        Map<String, AntenaInDTO> defaultMetrics = new HashMap<>();

        // Ordenar las antenas por su distancia
        antenas.sort(Comparator.comparing(AntenaInDTO::getDistance).thenComparing(antena -> antena.getMetrics().contains("") || antena.getMetrics().contains(null)));

        // Verificar si las métricas están vacías y agregar valores por defecto
        for (AntenaInDTO antena : antenas) {
            totalDistance += antena.getDistance();
            if (podName.equals("")) {
                podName = antena.getPod();
            }
            List<String> antenaMetrics = antena.getMetrics();
            boolean hasValidMetrics = antenaMetrics.stream().anyMatch(metric -> metric != null && !metric.isEmpty());
            if (!hasValidMetrics) {
                defaultMetrics.putIfAbsent(antena.getPod(), antena);
            }
            antenaMetrics.replaceAll(metric -> metric == null || metric.isEmpty() ? "N/A" : metric);
            for (String antenaMetric : antenaMetrics) {
                if (!antenaMetric.equals("N/A")) {
                    // Agregar la métrica en orden de prioridad
                    boolean added = false;
                    for (int j = 0; j < metrics.size(); j++) {
                        if (metrics.get(j).getValue().equals(antenaMetric)) {
                            added = true;
                            break;
                        }
                        if (metrics.get(j).getValue().equals("N/A")) {
                            metrics.set(j, new Metric(antenaMetric, antena.getDistance()));
                            added = true;
                            break;
                        }
                        if (antena.getDistance() < metrics.get(j).getDistance()) {
                            metrics.add(j, new Metric(antenaMetric, antena.getDistance()));
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        metrics.add(new Metric(antenaMetric, antena.getDistance()));
                    }
                }
            }
        }

        if (totalDistance == 0.0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> position = new HashMap<>();
        position.put("distance", totalDistance);
        position.put("distance_copy", totalDistance / antenas.size());

        Map<String, Object> response = new HashMap<>();
        response.put("pod", antenas.get(0).getPod());
        response.put("position", position);

        if (metrics.size() < 4 || metrics.stream().anyMatch(metric -> metric.getValue().equals(""))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        response.put("metrics", String.format("%s,%s,%s,%s", metrics.get(0).getValue(), metrics.get(1).getValue(), metrics.get(2).getValue(), metrics.get(3).getValue()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static class Metric {

        private final String value;
        private final Double distance;

        public Metric(String value, Double distance) {
            this.value = value;
            this.distance = distance;

        }

        public String getValue() {
            return value;
        }

        public Double getDistance() {
            return distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Metric metric = (Metric) o;
            return value.equals(metric.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
