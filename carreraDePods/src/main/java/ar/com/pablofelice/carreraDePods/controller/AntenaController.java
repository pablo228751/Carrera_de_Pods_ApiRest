package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.AntenaPosicionService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/podhealth/")
public class AntenaController {

    private final AntenaPosicionService antenaPositionCalculator;

    @Autowired
    public AntenaController(AntenaPosicionService antenaPositionCalculator) {
        this.antenaPositionCalculator = antenaPositionCalculator;
    }

    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ejemplo de cuerpo de solicitud",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = EXAMPLE_REQUEST_BODY, name = "ejemplo"))
    )
    public ResponseEntity<?> createAntenas(@RequestBody Map<String, List<AntenaInDTO>> body) {
        List<AntenaInDTO> antenas = body.get("antenas");

        return antenaPositionCalculator.calcularPosicionAntena(antenas);
    }

    private static final String EXAMPLE_REQUEST_BODY = "{\n" +
            "  \"antenas\": [\n" +
            "    {\n" +
            "      \"name\": \"antena0\",\n" +
            "      \"pod\": \"Anakin Skywalker\",\n" +
            "      \"distance\": 210.0,\n" +
            "      \"metrics\": [\n" +
            "        \"590C\",\n" +
            "        \"\",\n" +
            "        \"\",\n" +
            "        \"60%\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"antena1\",\n" +
            "      \"pod\": \"Anakin Skywalker\",\n" +
            "      \"distance\": 225.5,\n" +
            "      \"metrics\": [\n" +
            "        \"\",\n" +
            "        \"1MWh\",\n" +
            "        \"\",\n" +
            "        \"60%\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"antena2\",\n" +
            "      \"pod\": \"Anakin Skywalker\",\n" +
            "      \"distance\": 252.7,\n" +
            "      \"metrics\": [\n" +
            "        \"590C\",\n" +
            "        \"\",\n" +
            "        \"110C, \",\n" +
            "        \"\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
