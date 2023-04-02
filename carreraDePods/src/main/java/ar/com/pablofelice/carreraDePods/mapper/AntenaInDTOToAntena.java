/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.mapper;

import ar.com.pablofelice.carreraDePods.persistence.entity.DatosAntena;
import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import org.springframework.stereotype.Component;

@Component
public class AntenaInDTOToAntena implements IMapper<DatosAntenaInDto, DatosAntena> {

    @Override
    public DatosAntena mapToEntity(DatosAntenaInDto dto) {
        DatosAntena antena = new DatosAntena();
        antena.setName(dto.getName());
        antena.setPod(dto.getPod());
        antena.setDistance(dto.getDistance());
        antena.setMetrics(dto.getMetrics());
        return antena;
    }

    @Override
    public DatosAntenaInDto mapToDTO(DatosAntena entity) {
        DatosAntenaInDto dto = new DatosAntenaInDto();
        dto.setName(entity.getName());
        dto.setPod(entity.getPod());
        dto.setDistance(entity.getDistance());
        dto.setMetrics(entity.getMetrics());
        return dto;
    }
}
