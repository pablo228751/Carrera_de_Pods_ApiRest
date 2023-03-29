package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.events.AntenaCreatedEvent;
import ar.com.pablofelice.carreraDePods.events.Event;
import ar.com.pablofelice.carreraDePods.service.dto.AntenaInDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONObject;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

;

@Slf4j
@Component
public class GetEventsService {

    private final ConsumerFactory<String, Event<?>> consumerFactory;

    public GetEventsService(ConsumerFactory<String, Event<?>> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    public List<AntenaInDTO> getNombrePod(String pod) {
        List<AntenaInDTO> eventos = new ArrayList<>();
        try (Consumer<String, Event<?>> consumidor = consumerFactory.createConsumer()) {
            TopicPartition particionTopico = new TopicPartition("podhealth", 0);
            consumidor.assign(Collections.singletonList(particionTopico));
            long finOffset = consumidor.endOffsets(Collections.singletonList(particionTopico)).get(particionTopico);
            long inicioOffset = Math.max(0, finOffset - 10);
            consumidor.seek(particionTopico, inicioOffset);
            ConsumerRecords<String, Event<?>> registros = consumidor.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, Event<?>> registro : registros) {
                Event<?> evento = registro.value();
                if (evento instanceof AntenaCreatedEvent) {
                    //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%% INICIO %%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    AntenaCreatedEvent eventoAntenaCreada = (AntenaCreatedEvent) evento;
                    // Convertir los campos de AntenaCreatedEvent a los campos de AntenaInDTO, además filtrar solo las lecturas q
                    for (AntenaInDTO dto : eventoAntenaCreada.getData()) {

                        if (pod.equals(dto.getPod())) {
                            AntenaInDTO eventoAntena = new AntenaInDTO();
                            eventoAntena.setTimedate(dto.getTimedate());
                            eventoAntena.setName(dto.getName());
                            eventoAntena.setPod(dto.getPod());
                            eventoAntena.setDistance(dto.getDistance());
                            eventoAntena.setMetrics(dto.getMetrics());
                            eventos.add(eventoAntena);
                        }
                    }
                    //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%% FIN %%%%%%%%%%%%%%%%%%%%%%%%%%%");
                }
            }
        }
        return filtrar(eventos);
    }

    public List<AntenaInDTO> getNombreBodyJson(String body) {
        List<AntenaInDTO> eventos = new ArrayList<>();
        JSONObject requestBody = new JSONObject(body);
        String pod = requestBody.getString("pod");
        try (Consumer<String, Event<?>> consumidor = consumerFactory.createConsumer()) {
            TopicPartition particionTopico = new TopicPartition("podhealth", 0);
            consumidor.assign(Collections.singletonList(particionTopico));
            long finOffset = consumidor.endOffsets(Collections.singletonList(particionTopico)).get(particionTopico);
            long inicioOffset = Math.max(0, finOffset - 10);
            consumidor.seek(particionTopico, inicioOffset);
            ConsumerRecords<String, Event<?>> registros = consumidor.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, Event<?>> registro : registros) {
                Event<?> evento = registro.value();
                if (evento instanceof AntenaCreatedEvent) {
                    AntenaCreatedEvent eventoAntenaCreada = (AntenaCreatedEvent) evento;
                    // Convertir los campos de AntenaCreatedEvent a los campos de AntenaInDTO, además filtrar solo las lecturas q
                    for (AntenaInDTO dto : eventoAntenaCreada.getData()) {
                        if (pod.equals(dto.getName())) {
                            AntenaInDTO eventoAntena = new AntenaInDTO();
                            eventoAntena.setTimedate(dto.getTimedate());
                            eventoAntena.setName(dto.getName());
                            eventoAntena.setPod(dto.getPod());
                            eventoAntena.setDistance(dto.getDistance());
                            eventoAntena.setMetrics(dto.getMetrics());
                            eventos.add(eventoAntena);
                        }
                    }
                }
            }
        }
        return eventos;
    }

    // Filtrar los ultimo eventos encontrados (que corresponde con el nombre del POD) y seleccionar en una lista solo aquellos que tengan diferencia de "X" miliseg.
    public List<AntenaInDTO> filtrar(List<AntenaInDTO> eventos) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&& PRUEBAS PRUEBAS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        String ultimoRegistro;
        List<AntenaInDTO> eventosEnviar = new ArrayList<>();
        if (!eventos.isEmpty()) {
            //Se lee el ulltimo registro de la lista y se lo asigna con el formato strign
            ultimoRegistro = eventos.get(eventos.size() - 1).getTimedate();
            System.out.println("ultimoRegistro = " + ultimoRegistro);
            //se establece una variable para el parseo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                //se convierte la variable a tipo Date
                Date fechaUltimoRegistro = sdf.parse(ultimoRegistro);
                System.out.println("fechaUltimoRegistro = " + fechaUltimoRegistro);
                for (AntenaInDTO dto : eventos) {
                    System.out.println("####### COMPARANDO ######");
                    AntenaInDTO eventoAntena = new AntenaInDTO();
                    Date fechaEvento = sdf.parse(dto.getTimedate());
                    //Se compara para filtrar aquiellos con diferencia de 1 segundo
                    System.out.println("fechaUltimoRegistro.getTime() = " + fechaUltimoRegistro.getTime());
                    System.out.println("fechaEvento.getTime() = " + fechaEvento.getTime());
                    long diferenciaMilis = Math.abs(fechaUltimoRegistro.getTime() - fechaEvento.getTime());
                    System.out.println("diferenciaMilis = " + diferenciaMilis);
                    if (diferenciaMilis <= 1000) {
                        eventoAntena.setTimedate(dto.getTimedate());
                        eventoAntena.setName(dto.getName());
                        eventoAntena.setPod(dto.getPod());
                        eventoAntena.setDistance(dto.getDistance());
                        eventoAntena.setMetrics(dto.getMetrics());
                        eventosEnviar.add(eventoAntena);
                    }
                    System.out.println("#######  FIN COMPARANDO ######");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&& FIN FIN  PRUEBAS PRUEBAS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        return eventosEnviar;

    }

}
