package ar.com.pablofelice.carreraDePods.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Data
@Entity
@Table(name="antenas")
public class Antena {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String pod;
    private Double distance;
    private List<String> metrics;
    
}