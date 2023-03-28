package ar.com.pablofelice.carreraDePods.events;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public abstract class Event <T> {
    private String id;
    private Date date;
    private T data;
}