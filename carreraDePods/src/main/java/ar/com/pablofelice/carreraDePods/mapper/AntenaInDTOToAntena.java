/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.mapper;

import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import org.springframework.stereotype.Component;

@Component
public class AntenaInDTOToAntena implements IMapper<AntenaInDTO, Antena> {

    @Override
    public Antena mapToEntity(AntenaInDTO dto) {
        Antena antena = new Antena();
        antena.setName(dto.getName());
        antena.setPod(dto.getPod());
        antena.setDistance(dto.getDistance());
        antena.setMetrics(dto.getMetrics());
        return antena;
    }

    @Override
    public AntenaInDTO mapToDTO(Antena entity) {
        AntenaInDTO dto = new AntenaInDTO();
        dto.setName(entity.getName());
        dto.setPod(entity.getPod());
        dto.setDistance(entity.getDistance());
        dto.setMetrics(entity.getMetrics());
        return dto;
    }
}
