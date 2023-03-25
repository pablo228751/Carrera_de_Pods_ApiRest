package ar.com.pablofelice.carreraDePods.persistence.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "antena")
public class Antena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pod;
    private Double distance;
    @ElementCollection
    @CollectionTable(name = "antena_metrics")
    private List<String> metrics;

    public Antena() {
    }

    public Antena(String name, String pod, Double distance, List<String> metrics) {
        this.name = name;
        this.pod = pod;
        this.distance = distance;
        this.metrics = metrics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }
}
