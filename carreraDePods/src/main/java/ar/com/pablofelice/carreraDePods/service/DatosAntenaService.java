package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.DatosAntena;
import ar.com.pablofelice.carreraDePods.persistence.repository.AntenaRepository;
import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import ar.com.pablofelice.carreraDePods.utils.CrearAntena;
import ar.com.pablofelice.carreraDePods.utils.DistanciaAntena;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatosAntenaService {

    private final AntenaRepository antenaRepository;
    private final IMapper<DatosAntenaInDto, DatosAntena> antenaMapper;
    private final SubirEventsService subirEventsService;    
    private List<CrearAntena> listaAntenas = new ArrayList<>();
    private static boolean antenasFijas;

    @Autowired
    public DatosAntenaService(AntenaRepository antenaRepository, IMapper<DatosAntenaInDto, DatosAntena> antenaMapper, SubirEventsService subirEventsService) {
        this.antenaRepository = antenaRepository;
        this.antenaMapper = antenaMapper;
        this.subirEventsService = subirEventsService;
    }

    // Guardar datos en la base
    public void guardarDB(List<DatosAntenaInDto> antenaInDTOS) {
        List<DatosAntena> antenas = antenaInDTOS.stream()
                .map(antenaMapper::mapToEntity)
                .collect(Collectors.toList());
        antenaRepository.saveAll(antenas);
    }
    
    //Procesa Coordenadas y Métricas
    public ResponseEntity<?> datosAntena(List<DatosAntenaInDto> antenas, boolean subir) {
        String nombrePod = antenas.get(0).getPod();
        //Crear las antenas con valores Harcodeados en la clase
        if (!this.antenasFijas) {
            CrearAntena ant = new CrearAntena();
            this.listaAntenas = ant.antenasFijas(listaAntenas);
            CrearAntena.cantDeAntenasDisponibles = this.listaAntenas.size();
            CrearAntena.setListaDeNombres(this.listaAntenas);
            this.antenasFijas = true;
        }
        //ListaAntenas.add(new CrearAntena("Antena4", 800, 100));
        List<DistanciaAntena> listaDistancias = new ArrayList<>();
        for (DatosAntenaInDto antena : antenas) {
            listaDistancias.add(new DistanciaAntena(antena.getName(), antena.getDistance().floatValue()));
        }
        LocationAndMessage coor = new LocationAndMessage(this.listaAntenas);
        float[] resultX_Y = coor.getLocation(listaDistancias);
        if (resultX_Y == null) {
            return new ResponseEntity<>("RESPONSE CODE: 404 (Datos insuficientes)", HttpStatus.NOT_FOUND);
        }
        //System.out.println("***************************        FIN                 *****************************");
        List<String> metricasFinales = coor.getMessage(antenas);
        //Si MetricaFinal quedó  Vacía devuelve Error 404
        for (int i = 0; i < metricasFinales.size(); i++) {
            if (metricasFinales.get(i) == null || metricasFinales.get(i).isEmpty()) {
                return new ResponseEntity<>("RESPONSE CODE: 404", HttpStatus.NOT_FOUND);
            }
        }
        Map<String, Object> posicion = new HashMap<>();
        posicion.put("x", resultX_Y[0]);
        posicion.put("y", resultX_Y[1]);
        Map<String, Object> response = new HashMap<>();
        response.put("pod", nombrePod);
        response.put("position", posicion);
        //Response en formato String
        String metricsString = String.join(",", metricasFinales);
        response.put("metrics", metricsString);        
        //this.guardarDB(antenas); ---> No se va a usaren el proyecto
        if (subir){
            this.subirEventsService.subirPodHealth(antenas);
        }        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
