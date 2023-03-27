package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import ar.com.pablofelice.carreraDePods.utils.CrearAntena;
import ar.com.pablofelice.carreraDePods.utils.DistanciaAntena;
import java.util.List;
import lombok.Data;

@Data
public class MessageLocationService {

    /*
    Se crean X cantidad de objetos antenas, según recibe el Contructor,
    luego,se crea una Lista de distancias con tamaño <objetos antenas.size()>.
     */
    private List<CrearAntena> antena;
    float[] coordenadas = new float[2];
    private final int cantAntenas;

    public MessageLocationService(List<CrearAntena> antena) {
        this.antena = antena;
        this.cantAntenas = antena.size();
    }

    public float[] getLocation(List<DistanciaAntena> distanciaAntena) {
        if (distanciaAntena.size() < cantAntenas) {
            System.out.println("Datos insuficioentes");
            return null;
        }

        //Si hay más de 3 antenas se consulta otro metodo
        if (distanciaAntena.size() > 3) {
            return getLocation2(distanciaAntena);
        }
        // Cálculo de las coordenadas del vehículo en un plano bidimensional
        float x = (float) ((Math.pow(distanciaAntena.get(0).getDistancia(), 2) - Math.pow(distanciaAntena.get(1).getDistancia(), 2)
                + Math.pow(antena.get(1).getCoordenadas()[0], 2) - Math.pow(antena.get(0).getCoordenadas()[0], 2)
                + Math.pow(antena.get(1).getCoordenadas()[1], 2) - Math.pow(antena.get(0).getCoordenadas()[1], 2))
                / (2 * antena.get(1).getCoordenadas()[0] - 2 * antena.get(0).getCoordenadas()[0]));
        float y = (float) ((Math.pow(distanciaAntena.get(0).getDistancia(), 2) - Math.pow(distanciaAntena.get(2).getDistancia(), 2)
                + Math.pow(antena.get(2).getCoordenadas()[0], 2) - Math.pow(antena.get(0).getCoordenadas()[0], 2)
                + Math.pow(antena.get(2).getCoordenadas()[1], 2) - Math.pow(antena.get(0).getCoordenadas()[1], 2)
                - 2 * (antena.get(2).getCoordenadas()[1] - antena.get(0).getCoordenadas()[1]) * x)
                / (2 * (antena.get(2).getCoordenadas()[1] - antena.get(0).getCoordenadas()[1])));

        x = Math.round(x * 100.0f) / 100.0f; // Redondear a dos decimales
        y = Math.round(y * 100.0f) / 100.0f;

        System.out.format("***MessageLocationService**** getLocation dice: Las coordenadas del vehículo son: (%.2f, %.2f)\n", x, y);
        this.coordenadas[0] = x;
        this.coordenadas[1] = y;

        return this.coordenadas;
    }

    public float[] getLocation2(List<DistanciaAntena> distanciaAntena) {
        if (distanciaAntena.size() < cantAntenas) {
            System.out.println("Datos insuficientes");
            return null;
        }

        // Cálculo de las coordenadas del vehículo en un plano bidimensional
        float[] xArray = new float[cantAntenas];
        float[] yArray = new float[cantAntenas];
        float[] distanciaArray = new float[cantAntenas];

        for (int i = 0; i < cantAntenas; i++) {
            distanciaArray[i] = distanciaAntena.get(i).getDistancia();
            xArray[i] = antena.get(i).getCoordenadas()[0];
            yArray[i] = antena.get(i).getCoordenadas()[1];
        }

        float sumX = 0;
        float sumY = 0;
        float sumD = 0;

        for (int i = 0; i < cantAntenas; i++) {
            float distanciaRelativa = distanciaArray[i] / sumDistancias(distanciaArray, i);
            sumX += xArray[i] * distanciaRelativa;
            sumY += yArray[i] * distanciaRelativa;
            sumD += distanciaRelativa;
        }

        this.coordenadas[0] = Math.round(sumX / sumD * 100.0f) / 100.0f;
        this.coordenadas[1] = Math.round(sumY / sumD * 100.0f) / 100.0f;

        System.out.format("***MessageLocationService**** getLocation2 dice: Las coordenadas del vehículo son: (%.2f, %.2f)\n", this.coordenadas[0], this.coordenadas[1]);

        return this.coordenadas;
    }

    private float sumDistancias(float[] distanciaArray, int index) {
        float sum = 0;
        for (int i = 0; i < distanciaArray.length; i++) {
            if (i != index) {
                sum += distanciaArray[i] / distanciaArray[index];
            }
        }
        return sum + (distanciaArray.length - 2);
    }

    public List<String> getMessage(List<AntenaInDTO> antenas) {
        List<String> metricasFinales = antenas.get(0).getMetrics();
        for (AntenaInDTO antena : antenas) {
            //Completar Métricas Vacías
            for (int i = 0; i < metricasFinales.size(); i++) {
                if (metricasFinales.get(i) == null || metricasFinales.get(i).isEmpty()) {
                    for (AntenaInDTO otraAntena : antenas) {
                        if (otraAntena.getMetrics().get(i) != null && !otraAntena.getMetrics().get(i).isEmpty()) {
                            metricasFinales.set(i, otraAntena.getMetrics().get(i));
                            break;
                        }
                    }
                }
            }
        }
        return metricasFinales;
    }

}
