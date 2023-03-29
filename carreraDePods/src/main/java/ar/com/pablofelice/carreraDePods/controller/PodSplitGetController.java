package ar.com.pablofelice.carreraDePods.controller;

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

    @Autowired
    public PodSplitGetController(GetEventsService getEventsService) {
        this.getEventsService = getEventsService;
    }

    @GetMapping("/{antena_name}")
    public ResponseEntity<String> getPodHealthSplit(@PathVariable("antena_name") String antenaName) {
        System.out.println("Antena seleccionada: " + antenaName);
        List<AntenaInDTO> events = getEventsService.getNombrePod(antenaName);
        
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% INICIO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(events.toString());
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% FIN %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        // Procesar los eventos recibidos
        return ResponseEntity.ok("Antena seleccionada: " + antenaName);
    }
}

