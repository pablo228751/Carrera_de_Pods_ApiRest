package ar.com.pablofelice.carreraDePods.controller;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.AntenaService;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import ar.com.pablofelice.carreraDePods.service.dto.AntenasDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/podhealth")
public class AntenaController {

    private final AntenaService antenaService;

    @Autowired
    public AntenaController(AntenaService antenaService) {
        this.antenaService = antenaService;
    }

    @PostMapping
    public ResponseEntity<Void> createAntenas(@RequestBody AntenasDTO antenasDTO) {
        antenaService.saveAntenas(antenasDTO.getAntenas());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

