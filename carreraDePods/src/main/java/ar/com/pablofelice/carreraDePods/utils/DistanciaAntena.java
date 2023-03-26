package ar.com.pablofelice.carreraDePods.utils;

import lombok.Data;


@Data
public class DistanciaAntena {
    private String nombre;
    private float distancia;
    
    public DistanciaAntena(String nombre, float d){
        this.nombre = nombre;
        this.distancia = d;
    }
    
    
}
