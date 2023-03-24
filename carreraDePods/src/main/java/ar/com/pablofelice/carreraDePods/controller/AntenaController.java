package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.AntenaService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/podhealth")
public class AntenaController {
    private final AntenaService antenaService;

    public AntenaController(AntenaService antenaService) {
        this.antenaService = antenaService;
    }

    @PostMapping
    public Antena ingresoAntena(@RequestBody AntenaInDTO antenaInDTO){
        return this.antenaService.guardarAntena(antenaInDTO);        
    }

    @GetMapping("/")
    public ResponseEntity<?> obtenerAntena() {
        Map<String, Object> response = antenaService.obtenerAntena();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = "";
        try {
            json = mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }
}