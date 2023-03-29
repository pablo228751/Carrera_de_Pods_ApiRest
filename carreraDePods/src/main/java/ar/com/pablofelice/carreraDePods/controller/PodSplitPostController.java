package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.AntenaService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
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

    private final AntenaService antenaService;

    @Autowired
    public PodSplitPostController(AntenaService antenaService) {
        this.antenaService = antenaService;
    }

    @PostMapping("/{antena_name}")
    public ResponseEntity<String> processPodHealthSplit(@PathVariable("antena_name") String antenaName,
                                                        @RequestBody Map<String, List<AntenaInDTO>> body) {
        List<AntenaInDTO> antenas = body.get("antenas");
        antenaService.datosAntena(antenas);
        return ResponseEntity.ok("Success");
    }
}

