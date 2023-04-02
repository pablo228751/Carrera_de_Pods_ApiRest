package ar.com.pablofelice.carreraDePods.service;

import ar.com.pablofelice.carreraDePods.events.DatosCrearEvento;
import ar.com.pablofelice.carreraDePods.service.dto.DatosAntenaInDto;
import ar.com.pablofelice.carreraDePods.utils.CrearAntena;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetEventsService {

    private final ConsumerFactory<String, String> consumerFactory;

    public GetEventsService(ConsumerFactory<String, String> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    public List<DatosAntenaInDto> getNombrePod(String pod) {
        List<DatosAntenaInDto> eventos = new ArrayList<>();
        try (Consumer<String, String> consumidor = consumerFactory.createConsumer()) {
            TopicPartition particionTopico = new TopicPartition("podhealth", 0);
            consumidor.assign(Collections.singletonList(particionTopico));
            long finOffset = consumidor.endOffsets(Collections.singletonList(particionTopico)).get(particionTopico);
            long inicioOffset = Math.max(0, finOffset - 10);
            consumidor.seek(particionTopico, inicioOffset);
            ConsumerRecords<String, String> registros = consumidor.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> registro : registros) {
                System.out.println("Mensaje recibido: " + registro.value());
                ObjectMapper objectMapper = new ObjectMapper();
                DatosCrearEvento eventoAntenaCreada = objectMapper.readValue(registro.value(), DatosCrearEvento.class);
                for (DatosAntenaInDto dto : eventoAntenaCreada.getData()) {
                    if (pod.replaceAll("\\s", "").toLowerCase().equals(dto.getPod().replaceAll("\\s", "").toLowerCase())) {
                        DatosAntenaInDto eventoAntena = new DatosAntenaInDto();
                        eventoAntena.setTimedate(dto.getTimedate());
                        eventoAntena.setName(dto.getName());
                        eventoAntena.setPod(dto.getPod());
                        eventoAntena.setDistance(dto.getDistance());
                        eventoAntena.setMetrics(dto.getMetrics());
                        eventos.add(eventoAntena);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: "+ e);
        }
        return filtrar(eventos);
    }

    // Filtrar los ultimos eventos encontrados (que corresponde con el nombre del POD) y seleccionar en una lista solo aquellos que tengan diferencia de "X" miliseg.    
    public List<DatosAntenaInDto> filtrar(List<DatosAntenaInDto> eventos) {
        String primerRegistro;
        List<DatosAntenaInDto> eventosFiltrar = new ArrayList<>();
        if (!eventos.isEmpty()) {
            // Para ordenar de más reciente a mas antiguo
            Collections.sort(eventos, (a1, a2) -> a2.getTimedate().compareTo(a1.getTimedate()));
            //Se lee el ulltimo registro de la lista y se lo asigna con el formato strign
            primerRegistro = eventos.get(0).getTimedate();
            //se establece una variable para el parseo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                //se convierte la variable a tipo Date
                Date fechaUltimoRegistro = sdf.parse(primerRegistro);
                for (DatosAntenaInDto dto : eventos) {
                    DatosAntenaInDto eventoAntena = new DatosAntenaInDto();
                    Date fechaEvento = sdf.parse(dto.getTimedate());
                    //Se compara para filtrar aquiellos con diferencia de 1 segundo (Para pruebas esta seteado en 2 segundos)
                    long diferenciaMilis = Math.abs(fechaUltimoRegistro.getTime() - fechaEvento.getTime());
                    if (diferenciaMilis <= 2000) {
                        eventoAntena.setTimedate(dto.getTimedate());
                        eventoAntena.setName(dto.getName());
                        eventoAntena.setPod(dto.getPod());
                        eventoAntena.setDistance(dto.getDistance());
                        eventoAntena.setMetrics(dto.getMetrics());
                        eventosFiltrar.add(eventoAntena);
                    }
                }
            } catch (ParseException e) {
                System.out.println("e = " + e);
            }
        }
        List<String> nombreAntenas;
        nombreAntenas = CrearAntena.listaDeNombres;
        //Eliminar de elementos de la lista si es que ya hay una antena con el mismo nombre con una lectura cargada
        List<DatosAntenaInDto> eventosEnviar = new ArrayList<>();
        for (String nombreAntena : nombreAntenas) {
            for (DatosAntenaInDto evento : eventosFiltrar) {
                if (evento.getName().equals(nombreAntena)) {
                    eventosEnviar.add(evento);
                    break;
                }
            }
        }
        return eventosEnviar;
    }
}
