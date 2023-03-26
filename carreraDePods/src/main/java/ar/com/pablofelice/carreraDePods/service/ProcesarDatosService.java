/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
public class ProcesarDatosService {

    public List<String> getMetricasPorDefecto(List<Metrica> metricas, List<AntenaInDTO> antenas) {
    List<String> metricasPorDefecto = new ArrayList<>(Arrays.asList("N/A", "N/A", "N/A", "N/A"));
    int i = 0;
    for (Metrica metrica : metricas) {
        if (metrica.getValor() == null || metrica.getValor().isEmpty()) {
            if (i == antenas.size() - 1) {
                // Si no se encontró una métrica válida en la última antena, entonces no se pueden calcular las métricas.
                // Devolver un mensaje de error.
                return metricasPorDefecto;
            }
            for (int j = i + 1; j < antenas.size(); j++) {
                AntenaInDTO siguienteAntena = antenas.get(j);
                List<String> metricasSiguienteAntena = siguienteAntena.getMetrics();
                for (int k = 0; k < metricasSiguienteAntena.size(); k++) {
                    if (!metricasSiguienteAntena.get(k).equals("N/A") && !metricasPorDefecto.contains(metricasSiguienteAntena.get(k))) {
                        metricasPorDefecto.set(i, metricasSiguienteAntena.get(k));
                        i++;
                        if (i == 4) {
                            break;
                        }
                    }
                }
                if (i == 4) {
                    break;
                }
            }
        } else {
            if (!metricasPorDefecto.contains(metrica.getValor())) {
                metricasPorDefecto.set(i, metrica.getValor());
                i++;
            }
        }
        if (i == 4) {
            break;
        }
    }
    
    
    return metricasPorDefecto;
}


    public static class Metrica {

        private final String valor;
        private final Double distancia;

        public Metrica(String valor, Double distancia) {
            this.valor = valor;
            this.distancia = distancia;
        }

        public String getValor() {
            return valor;
        }

        public Double getDistancia() {
            return distancia;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Metrica metrica = (Metrica) o;
            return valor.equals(metrica.valor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valor);
        }
    }
}
