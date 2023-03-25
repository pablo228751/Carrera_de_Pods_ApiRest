package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.service.AntenaPosicionService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/podhealth")
public class AntenaController {

    private final AntenaPosicionService antenaPositionCalculator;

    @Autowired
    public AntenaController(AntenaPosicionService antenaPositionCalculator) {
        this.antenaPositionCalculator = antenaPositionCalculator;
    }

    @PostMapping
    public ResponseEntity<?> createAntenas(@RequestBody Map<String, List<AntenaInDTO>> body) {
        List<AntenaInDTO> antenas = body.get("antenas");

        return antenaPositionCalculator.calculateAntenaPosition(antenas);
    }
}
