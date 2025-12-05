package com.exadel.pedrolima.userservice.kafka;

import com.exadel.pedrolima.userservice.event.TicketConfirmationEvent;
import com.exadel.pedrolima.userservice.event.TicketReservationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketConfirmationConsumer {

    @KafkaListener(topics = {"ticket-confirmed", "ticket-rejected"}, groupId = "user-service-group")
    public void consumeTicketConfirmation(TicketConfirmationEvent event) {
        if(event.isConfirmed()) {
            System.out.println("Confirmed reservation: " + event);
        } else{
            System.out.println("Rejected reservation: " + event);
        }
    }
}
