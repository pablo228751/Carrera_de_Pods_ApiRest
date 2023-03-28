/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.mapper.IMapper;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubirTopicService {

    private final SubirEventsService antenaEventsService;
    private final IMapper<AntenaInDTO, Antena> antenaMapper;

    @Autowired
    public SubirTopicService(IMapper<AntenaInDTO, Antena> antenaMapper, SubirEventsService antenaEventsService) {
        this.antenaEventsService = antenaEventsService;
        this.antenaMapper = antenaMapper;
    }

    //Subir podhealth
    public void subirPodhealth(List<AntenaInDTO> antenaInDTOS) {
        List<Antena> antenas = antenaInDTOS.stream()
                .map(antenaMapper::mapToEntity)
                .collect(Collectors.toList());
        System.out.println("saveKafka dice: Recibido " + antenas);
        this.antenaEventsService.subirPodHealth(antenas);
    }

}
