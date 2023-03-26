/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.utils;

import lombok.Data;

/**
 *
 * @author Usuario
 */
@Data
public class CrearAntena {
    String nombre;
    float[] coordenadas = new float[2];

    public CrearAntena(String nombre, float coordenadaX, float coordenadaY) {
        this.nombre = nombre;
        this.coordenadas[0] = coordenadaX;
        this.coordenadas[1] = coordenadaY;
    }
    
    
}
