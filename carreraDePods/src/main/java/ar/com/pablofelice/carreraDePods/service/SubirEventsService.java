/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.events.DatosCrearEvento;
import ar.com.pablofelice.carreraDePods.events.Evento;
import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.kafka.core.ProducerFactory;

@Component
public class SubirEventsService {

    private KafkaTemplate<String, Evento<?>> producer;

    public SubirEventsService(ProducerFactory<String, Evento<?>> producer) {
        this.producer = new KafkaTemplate<>(producer);
    }

    @Value("${topic.podhealth.name:podhealth}")
    private String topicPodHealth;

    public void subirPodHealth(List<DatosAntenaInDto> antenas) {
        DatosCrearEvento enviarAKafka = new DatosCrearEvento(antenas);
        enviarAKafka.setId(UUID.randomUUID().toString());
        enviarAKafka.setDate(new Date());

        this.producer.send(topicPodHealth, enviarAKafka);
    }

    public void subirPodHealth(DatosAntenaInDto antenas) {
        DatosCrearEvento enviarAKafka = new DatosCrearEvento(antenas);
        enviarAKafka.setId(UUID.randomUUID().toString());
        enviarAKafka.setDate(new Date());

        this.producer.send(topicPodHealth, enviarAKafka);
    }

}
