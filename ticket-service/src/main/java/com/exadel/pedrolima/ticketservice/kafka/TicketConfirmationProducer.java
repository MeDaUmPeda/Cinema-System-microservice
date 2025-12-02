package com.exadel.pedrolima.ticketservice.kafka;

import com.exadel.pedrolima.ticketservice.event.TicketConfirmationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketConfirmationProducer {

    private final KafkaTemplate<String, TicketConfirmationEvent> kafkaTemplate;

    public TicketConfirmationProducer(KafkaTemplate<String, TicketConfirmationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendConfirmation(TicketConfirmationEvent event) {
        kafkaTemplate.send("ticket-confirmed", event);
        System.out.println("Ticket confirmed send: " + event);
    }

    public void sendRejection(TicketConfirmationEvent event) {
        kafkaTemplate.send("ticket-rejected", event);
        System.out.println("Ticket rejected send: " + event);
    }
}
