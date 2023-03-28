
package ar.com.pablofelice.carreraDePods.service;


import ar.com.pablofelice.carreraDePods.events.AntenaCreatedEvent;
import ar.com.pablofelice.carreraDePods.events.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetEventsService {

	@KafkaListener(
			topics = "${topic.podhealth.name:podhealth}",
			containerFactory = "kafkaListenerContainerFactory",
	groupId = "grupo1")
	public void getTopic(Event<?> nvoEvent) {
		if (nvoEvent.getClass().isAssignableFrom(AntenaCreatedEvent.class)) {
                    System.out.println("#################################################### GET_KAFKA #######################################");
			AntenaCreatedEvent evento = (AntenaCreatedEvent) nvoEvent;
			log.info("GetEventsService dice: Evento recibido: ID={}, data={}",
					evento.getId(),
					evento.getData().toString());
                    System.out.println("################################################## FIN GET_KAFKA #######################################");
		}

	}
	
	

}
