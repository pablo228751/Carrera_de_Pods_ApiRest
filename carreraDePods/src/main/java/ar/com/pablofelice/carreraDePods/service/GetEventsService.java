package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.events.AntenaCreatedEvent;
import ar.com.pablofelice.carreraDePods.events.Event;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetEventsService {

    private final ConsumerFactory<String, Event<?>> consumerFactory;

    public GetEventsService(ConsumerFactory<String, Event<?>> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    public List<Event<?>> getTopic(String topic) {
        System.out.println("entre");
        List<Event<?>> events = new ArrayList<>();
        try (Consumer<String, Event<?>> consumer = consumerFactory.createConsumer()) {
            TopicPartition topicPartition = new TopicPartition(topic, 0);
            consumer.assign(Collections.singletonList(topicPartition));
            long endOffset = consumer.endOffsets(Collections.singletonList(topicPartition)).get(topicPartition);
            long startOffset = Math.max(0, endOffset - 10);
            consumer.seek(topicPartition, startOffset);
            ConsumerRecords<String, Event<?>> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, Event<?>> record : records) {
                Event<?> event = record.value();
                if (event instanceof AntenaCreatedEvent) {
                    AntenaCreatedEvent antenaCreatedEvent = (AntenaCreatedEvent) event;
                    log.info("AntenaCreatedEvent: id={}, data={}", antenaCreatedEvent.getId(), antenaCreatedEvent.getData().toString());
                }
            }
        }
        System.out.println("saliii");
        return events;
    }
}
