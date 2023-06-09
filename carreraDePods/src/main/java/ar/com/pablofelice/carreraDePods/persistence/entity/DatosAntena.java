package ar.com.pablofelice.carreraDePods.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "antena")
public class DatosAntena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pod;
    private Double distance;
    private List<String> metrics;

    public DatosAntena() {
    }

    public DatosAntena(String name, String pod, Double distance, List<String> metrics) {
        this.name = name;
        this.pod = pod;
        this.distance = distance;
        this.metrics = metrics;
    }
}
