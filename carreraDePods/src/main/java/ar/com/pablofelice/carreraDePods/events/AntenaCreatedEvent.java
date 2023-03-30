/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.events;


import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = true)
public class AntenaCreatedEvent extends Event<List<AntenaInDTO>> {
    @JsonCreator
    public AntenaCreatedEvent() {
        super(Collections.emptyList());
    }

    public AntenaCreatedEvent(AntenaInDTO antena) {
        super(Collections.singletonList(antena));
    }

    public AntenaCreatedEvent(List<AntenaInDTO> antenas) {
        super(antenas);
    }
}
