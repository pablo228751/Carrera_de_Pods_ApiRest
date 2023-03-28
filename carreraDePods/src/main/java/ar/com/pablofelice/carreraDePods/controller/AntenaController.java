package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import ar.com.pablofelice.carreraDePods.config.SwaggerConfig;
import ar.com.pablofelice.carreraDePods.service.AntenaService;
import ar.com.pablofelice.carreraDePods.service.SubirTopicService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/podhealth/")
public class AntenaController {

    private final SubirTopicService subirPodHealth;
    private final AntenaService antenaService;

    @Autowired
    public AntenaController(SubirTopicService subirPodHealth, AntenaService antenaService) {
        this.subirPodHealth = subirPodHealth;
        this.antenaService = antenaService;
    }

    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ejemplo de cuerpo de solicitud",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = SwaggerConfig.ejemploBodyPodhealth, name = "ejemplo"))
    )
    public ResponseEntity<?> createAntenas(@RequestBody Map<String, List<AntenaInDTO>> body) {
        List<AntenaInDTO> antenas = body.get("antenas");
        this.subirPodHealth.subirPodhealth(antenas);
        return antenaService.datosAntena(antenas);
        
    }

}
