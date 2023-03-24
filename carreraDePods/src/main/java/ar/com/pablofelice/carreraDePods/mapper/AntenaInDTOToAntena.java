/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.mapper;

import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import org.springframework.stereotype.Component;

/**
 *
 * @author Usuario
 */
@Component
public class AntenaInDTOToAntena implements IMapper<AntenaInDTO, Antena> {

    @Override
    public Antena map(AntenaInDTO in) {
        Antena antena = new Antena();
        antena.setName(in.getName());
        antena.setPod(in.getPod());
        antena.setDistance(in.getDistance());
        antena.setMetrics(in.getMetrics());
        return antena;
    }
   
    
}
