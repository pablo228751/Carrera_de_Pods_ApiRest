package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.AntenaInDTOToAntena;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import ar.com.pablofelice.carreraDePods.persistence.repository.AntenaRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service
public class AntenaService {
    private final AntenaRepository antenaRepository;
    private final AntenaInDTOToAntena mapper;

    public AntenaService(AntenaRepository antenaRepository, AntenaInDTOToAntena mapper) {
        this.antenaRepository = antenaRepository;
        this.mapper = mapper;
    }

    public Antena guardarAntena(AntenaInDTO antenaInDto) {
        // Recibe objeto antenaInDto y se debe transormar en un obj Antena para enviarse como parametro
        //Usamos mapper
        Antena antena = this.mapper.map(antenaInDto);
        return this.antenaRepository.save(antena);
    }

    public Map<String, Object> obtenerAntena() {
        String pod = "Anakin Skywalker";
        double x = -210.0;
        double y = 95.5;
        String metrics = "590C,1MWh, 110C, 60%";
        Map<String, Object> response = new HashMap<>();
        response.put("pod", pod);
        Map<String, Double> position = new HashMap<>();
        position.put("x", x);
        position.put("y", y);
        response.put("position", position);
        response.put("metrics", metrics);
        return response;
    }
}