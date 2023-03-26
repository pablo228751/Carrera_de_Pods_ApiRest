package ar.com.pablofelice.carreraDePods.service.dto;

import java.util.List;


public class AntenaInDTO {

    private String name;
    private String pod;
    private Double distance;
    private List<String> metrics;

    public AntenaInDTO() {
    }

    public AntenaInDTO(String name, String pod, Double distance, List<String> metrics) {
        this.name = name;
        this.pod = pod;
        this.distance = distance;
        this.metrics = metrics;
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