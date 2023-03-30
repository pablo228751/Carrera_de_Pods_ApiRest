package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.config.SwaggerConfig;
import ar.com.pablofelice.carreraDePods.service.AntenaService;
import ar.com.pablofelice.carreraDePods.service.GetEventsService;
import ar.com.pablofelice.carreraDePods.service.SubirEventsService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/podhealth_split")
public class PodSplitPostController {

    private final SubirEventsService subirEventsService;
    private final GetEventsService getEventsService;
    private final AntenaService antenaService;

    @Autowired
    public PodSplitPostController(SubirEventsService subirEventsService, GetEventsService getEventsService, AntenaService antenaService) {
        this.getEventsService = getEventsService;
        this.subirEventsService = subirEventsService;
        this.antenaService = antenaService;

    }

    @PostMapping("/{antena_name}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ejemplo de cuerpo de solicitud",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = SwaggerConfig.ejemploBodyPodhealthSplit, name = "ejemplo_body"))
    )
    public ResponseEntity<String> processPodHealthSplit(@PathVariable("antena_name") String antenaName,
            @RequestBody Map<String, Object> body) {
        String pod = (String) body.get("pod");
        Double distance = (Double) body.get("distance");
        List<String> message = (List<String>) body.getOrDefault("message", body.get("metrics"));
        AntenaInDTO antena = new AntenaInDTO();
        antena.setName(antenaName);
        antena.setPod(pod);
        antena.setDistance(distance);
        antena.setMetrics(message);
        this.subirEventsService.subirPodHealth(antena);
        //Buscar los topics que coincidan con el pod de la antena que llego por POST
        List<AntenaInDTO> events = getEventsService.getNombrePod(pod);
        /*
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% INICIO DE POST %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(events.toString());
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% FIN DE POST %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
         */
        //La respuestas proveniente de los topic Kafka aparte de contenido deben ser igual o mayor a 3 que es lo minímo para el cálculo trilateracio /triangulación
        if (events != null && !events.isEmpty() && events.size() >= 3) {
            return (ResponseEntity<String>) antenaService.datosAntena(events);
        }
        return ResponseEntity.badRequest().body("No hay suficiente información para procesar la solicitud");

    }
}
