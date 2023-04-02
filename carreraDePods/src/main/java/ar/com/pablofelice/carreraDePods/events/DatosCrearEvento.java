/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.events;


import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatosCrearEvento extends Evento<List<DatosAntenaInDto>> {
    @JsonCreator
    public DatosCrearEvento() {
        super(Collections.emptyList());
    }

    public DatosCrearEvento(DatosAntenaInDto antena) {
        super(Collections.singletonList(antena));
    }

    public DatosCrearEvento(List<DatosAntenaInDto> antenas) {
        super(antenas);
    }
}
