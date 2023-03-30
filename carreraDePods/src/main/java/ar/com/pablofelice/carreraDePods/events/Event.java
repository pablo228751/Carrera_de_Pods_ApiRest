package ar.com.pablofelice.carreraDePods.events;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Event<T> {
    private String id;
    private Date date;
    private T data;

    public Event(T data) {
        this.data = data;
    }
}