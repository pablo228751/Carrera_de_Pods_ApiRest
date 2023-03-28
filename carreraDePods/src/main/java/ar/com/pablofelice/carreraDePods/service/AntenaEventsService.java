/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.service;


import ar.com.pablofelice.carreraDePods.events.AntenaCreatedEvent;
import ar.com.pablofelice.carreraDePods.events.Event;
import ar.com.pablofelice.carreraDePods.persistence.entity.Antena;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.kafka.core.ProducerFactory;

@Component
public class AntenaEventsService {
    /*
   @Autowired
	private KafkaTemplate<String, Event<?>> producer;
*/
   
    
    private KafkaTemplate<String, Event<?>> producer;
 
   public AntenaEventsService(ProducerFactory<String, Event<?>> producer) {
      this.producer = new KafkaTemplate<>(producer);
         }
  
	
	@Value("${topic.antena.name:antena}")
	private String topicAntena;
	
	public void publish(List<Antena> antenas) {
                AntenaCreatedEvent enviarAKafka = new AntenaCreatedEvent();
		enviarAKafka.setData(antenas);
		enviarAKafka.setId(UUID.randomUUID().toString());
		enviarAKafka.setDate(new Date());

		this.producer.send(topicAntena, enviarAKafka);

		
	}
    

	
	

}
