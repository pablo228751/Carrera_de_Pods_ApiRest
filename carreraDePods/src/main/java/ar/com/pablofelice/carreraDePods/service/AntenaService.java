package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.persistence.repository.AntenaRepository;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
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
public class AntenaService {

    private final AntenaRepository antenaRepository;
    private final IMapper<AntenaInDTO, Antena> antenaMapper;
    private final SubirEventsService subirEventsService;
    
    // Constructor que toma todos los parámetros
    @Autowired
    public AntenaService(AntenaRepository antenaRepository, IMapper<AntenaInDTO, Antena> antenaMapper, SubirEventsService subirEventsService) {
        this.antenaRepository = antenaRepository;
        this.antenaMapper = antenaMapper;
        this.subirEventsService = subirEventsService;
        
    }   
    

    // Guardar datos en la base
    public void saveDB(List<AntenaInDTO> antenaInDTOS) {
        List<Antena> antenas = antenaInDTOS.stream()
                .map(antenaMapper::mapToEntity)
                .collect(Collectors.toList());
        antenaRepository.saveAll(antenas);
    }
    

    public ResponseEntity<?> datosAntena(List<AntenaInDTO> antenas) {
        //Toma nombre del primer objeto
        String nombrePod = antenas.get(0).getPod();

        //System.out.println("************************** PROBANDO COORDENADAS ************************************");
        List<CrearAntena> ListaAntenas = new ArrayList<CrearAntena>();
        ListaAntenas.add(new CrearAntena("Antena1", -500, -200));
        ListaAntenas.add(new CrearAntena("Antena2", 100, -100));
        ListaAntenas.add(new CrearAntena("Antena3", 500, 100));
        //ListaAntenas.add(new CrearAntena("Antena4", 800, 100));
        List<DistanciaAntena> listaDistancias = new ArrayList<DistanciaAntena>();
        for (AntenaInDTO antena : antenas) {
            listaDistancias.add(new DistanciaAntena(antena.getName(), antena.getDistance().floatValue()));
        }
        MessageLocationService coor = new MessageLocationService(ListaAntenas);
        float[] resultX_Y = coor.getLocation2(listaDistancias);
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
        //Response como Array
        //response.put("metrics", metricasFinales);
        
        this.saveDB(antenas);        
        this.subirEventsService.subirPodHealth(antenas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
