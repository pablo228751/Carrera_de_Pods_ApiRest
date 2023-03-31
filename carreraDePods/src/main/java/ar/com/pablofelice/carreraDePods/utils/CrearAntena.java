package ar.com.pablofelice.carreraDePods.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CrearAntena {

    private String nombre;
    private float[] coordenadas = new float[2];
    public static int cantDeAntenasDisponibles;
    public static List<String> listaDeNombres = new ArrayList<>();
    
    public CrearAntena(){
        
    }

    public CrearAntena(String nombre, float coordenadaX, float coordenadaY) {
        this.nombre = nombre;
        this.coordenadas[0] = coordenadaX;
        this.coordenadas[1] = coordenadaY;

    }

    public List<CrearAntena> antenasFijas(List<CrearAntena> antenas) {
        if (cantDeAntenasDisponibles <= 0) {
            antenas.add(new CrearAntena("antena0", -500, -200));
            antenas.add(new CrearAntena("antena1", 100, -100));
            antenas.add(new CrearAntena("antena2", 500, 100));
            return antenas;
        }
        return null;
    }

    public static void setListaDeNombres(List<CrearAntena> lista) {
        for (CrearAntena nombre : lista) {
            if (!listaDeNombres.contains(nombre.getNombre())) {
                listaDeNombres.add(nombre.getNombre());
            }
        }
    }

    public static void setListaDeNombres(String nombre) {
        if (!listaDeNombres.contains(nombre)) {
            listaDeNombres.add(nombre);
        }
    }

    public void estadoAntenas(List<String> nombres) {
        if (listaDeNombres != null && !listaDeNombres.isEmpty()) {
            for (String nombre : nombres) {
                if (!listaDeNombres.contains(nombre)) {
                    listaDeNombres.add(nombre);
                }
            }

        } else {
            listaDeNombres = nombres;
        }
        //Actualizar la cantidad de antenas actuales:
        cantDeAntenasDisponibles = listaDeNombres.size();

    }

    public void resetAntenas() {
        listaDeNombres = null;
        cantDeAntenasDisponibles = 0;
    }

}
