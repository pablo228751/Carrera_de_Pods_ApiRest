package ar.com.pablofelice.carreraDePods.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AntenaInDTO {

    private String name;
    private String pod;
    private Double distance;
    private List<String> metrics;
    private String timedate;

    public AntenaInDTO() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.timedate = sdf.format(new Date());
    }

    public AntenaInDTO(String name, String pod, Double distance, List<String> metrics) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.name = name;
        this.pod = pod;
        this.distance = distance;
        this.metrics = metrics;
        this.timedate = sdf.format(new Date());
    }

    @Override
    public String toString() {

        String cadena = "timedate= " + timedate +" name= " + name + "\n pod= " + pod + "\n distance= " + distance + "\n metrics" + metrics.toString();
        return cadena;
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
    public String getTimedate() {
        return timedate;
    }
     public void setTimedate(String timedate) {
        this.timedate = timedate;
    }
}
