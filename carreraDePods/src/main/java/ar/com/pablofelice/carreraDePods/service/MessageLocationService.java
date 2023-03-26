package ar.com.pablofelice.carreraDePods.service;

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
        this.cantAntenas= antena.size();
    }

    public float[] getLocation(List<DistanciaAntena> distanciaAntena) {
        if(distanciaAntena.size() != cantAntenas){
            System.out.println("Datos insuficioentes");
            return null;
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

        System.out.printf("***MessageLocationService**** Dice: Las coordenadas del vehículo son: (%.2f, %.2f)\n", x, y);
        coordenadas[0]=x;
        coordenadas[1]=y;        

        return coordenadas;
    }

    public void getMessage() {
        System.out.println("");
    }

}
