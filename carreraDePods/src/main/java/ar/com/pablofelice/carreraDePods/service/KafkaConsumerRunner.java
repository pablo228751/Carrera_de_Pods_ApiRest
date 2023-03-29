/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.events.AntenaCreatedEvent;
import ar.com.pablofelice.carreraDePods.events.Event;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

public class KafkaConsumerRunner  {

/*
public class KafkaConsumerRunner implements Runnable {
    

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaConsumer<String, String> consumer;

    public KafkaConsumerRunner(Properties props) {
        this.consumer = new KafkaConsumer<>(props);
    }
    
    public void run() {
        try {
            consumer.subscribe(Collections.singletonList("podhealth"));
            while (!closed.get()) {
                ConsumerRecords<String, Event<? extends Object>> records = (ConsumerRecords<String, Event<? extends Object>>) (ConsumerRecords<?, ?>) consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, Event<?>> record : records) {
                    if (record.value().getClass().isAssignableFrom(AntenaCreatedEvent.class)) {
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% INICIO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                        AntenaCreatedEvent evento = (AntenaCreatedEvent) record.value();
                        log.info("KafkaConsumerRunner dice: Evento recibido: ID={}, data={}",
                                evento.getId(),
                                evento.getData().toString());
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% FIN %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    }
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }

    // Método para cerrar el consumidor
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
*/
}
