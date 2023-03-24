package ar.com.pablofelice.carreraDePods.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class AntenaInDTO {

    private String name;
    private String pod;
    private Double distance;
    private List<String> metrics;

}
