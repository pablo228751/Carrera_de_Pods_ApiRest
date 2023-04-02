package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import ar.com.pablofelice.carreraDePods.config.SwaggerConfig;
import ar.com.pablofelice.carreraDePods.service.DatosAntenaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/podhealth/")
public class PostDatosController {
    
    private final DatosAntenaService antenaService;

    @Autowired
    public PostDatosController(DatosAntenaService antenaService) {
        this.antenaService = antenaService;
    }

    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ejemplo de cuerpo de solicitud",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = SwaggerConfig.ejemploBodyPodhealth, name = "/podhealth/"))
    )
    public ResponseEntity<?> podhealth(@RequestBody Map<String, List<DatosAntenaInDto>> body) {
        List<DatosAntenaInDto> antenas = body.get("antenas");
        return antenaService.datosAntena(antenas,true);
    }
}
