package com.exadel.pedrolima.userservice.kafka;

import com.exadel.pedrolima.userservice.event.TicketReservationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketReservationProducer {

    private final KafkaTemplate<String, TicketReservationEvent> kafkaTemplate;

    public TicketReservationProducer(KafkaTemplate<String, TicketReservationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTicketReservation(TicketReservationEvent event) {
        System.out.println("Sending booking event: " + event);
        kafkaTemplate.send("ticketReservation", event);
    }
}
