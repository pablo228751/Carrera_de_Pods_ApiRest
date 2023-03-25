/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;


/**
 *
 * @author Usuario
 */
@Service
public class ProcesarDatosService {
    
    public List<String> getDefaultMetrics(List<Metric> metrics, List<AntenaInDTO> antenas) {
        String[] defaultMetrics = {"N/A", "N/A", "N/A", "N/A"};
        int i = 0;
        for (Metric metric : metrics) {
            if (metric.getValue() == null || metric.getValue().isEmpty()) {
                if (i == antenas.size() - 1) {
                    // Si no se encontró una métrica válida en la última antena, entonces no se pueden calcular las métricas.
                    // Devolver un mensaje de error.
                    return Arrays.asList(defaultMetrics);
                }
                AntenaInDTO nextAntena = antenas.get(i + 1);
                List<String> nextAntenaMetrics = nextAntena.getMetrics();
                for (String nextAntenaMetric : nextAntenaMetrics) {
                    if (!nextAntenaMetric.equals("N/A")) {
                        defaultMetrics[i] = nextAntenaMetric;
                        i++;
                        if (i == 4) {
                            break;
                        }
                    }
                }
            } else {
                defaultMetrics[i] = metric.getValue();
                i++;
            }
            if (i == 4) {
                break;
            }
        }
        return Arrays.asList(defaultMetrics);
    }

    public static class Metric {

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
