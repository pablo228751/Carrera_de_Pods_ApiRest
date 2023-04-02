package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.DatosAntenaService;
import ar.com.pablofelice.carreraDePods.service.GetEventsService;
import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/podhealth_split")
@CrossOrigin(origins = "*")
public class GetPodSplitController {

    private final GetEventsService getEventsService;
    private final DatosAntenaService antenaService;

    @Autowired
    public GetPodSplitController(GetEventsService getEventsService, DatosAntenaService antenaService) {
        this.getEventsService = getEventsService;
        this.antenaService = antenaService;
    }
    
    @GetMapping("/{pod_name}")
    public ResponseEntity<String> getPodHealthSplit(@PathVariable("pod_name") String podName) {
        System.out.println("Antena seleccionada: " + podName);
        List<DatosAntenaInDto> events = getEventsService.getNombrePod(podName);
        //La respuestas proveniente de los topic Kafka aparte de contenido deben ser igual o mayor a 3 que es lo minímo para el cálculo trilateracio /triangulación
        if (events != null && !events.isEmpty() && events.size() >= 3) {
            return (ResponseEntity<String>) antenaService.datosAntena(events,false);
        }
        return ResponseEntity.badRequest().body("No hay suficiente información para procesar la solicitud");
    }
}
