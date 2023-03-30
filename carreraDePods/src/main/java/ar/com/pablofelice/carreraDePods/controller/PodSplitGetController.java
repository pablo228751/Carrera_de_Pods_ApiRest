package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.AntenaService;
import ar.com.pablofelice.carreraDePods.service.GetEventsService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/podhealth_split")
public class PodSplitGetController {

    private final GetEventsService getEventsService;
    private final AntenaService antenaService;

    @Autowired
    public PodSplitGetController(GetEventsService getEventsService, AntenaService antenaService) {
        this.getEventsService = getEventsService;
        this.antenaService = antenaService;
    }
    

    @GetMapping("/{pod_name}")
    public ResponseEntity<String> getPodHealthSplit(@PathVariable("pod_name") String podName) {
        System.out.println("Antena seleccionada: " + podName);
        List<AntenaInDTO> events = getEventsService.getNombrePod(podName);
        /*
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% INICIO DE GET %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(events.toString());
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% FIN DE GET%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
         */
        //La respuestas proveniente de los topic Kafka aparte de contenido deben ser igual o mayor a 3 que es lo minímo para el cálculo trilateracio /triangulación
        if (events != null && !events.isEmpty() && events.size() >= 3) {
            return (ResponseEntity<String>) antenaService.datosAntena(events);
        }
        return ResponseEntity.badRequest().body("No hay suficiente información para procesar la solicitud");
    }
}
