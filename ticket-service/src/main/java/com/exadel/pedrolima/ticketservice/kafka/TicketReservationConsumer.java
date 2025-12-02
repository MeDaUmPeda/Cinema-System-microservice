package com.exadel.pedrolima.ticketservice.kafka;

import com.exadel.pedrolima.ticketservice.event.TicketConfirmationEvent;
import com.exadel.pedrolima.ticketservice.event.TicketReservationEvent;
import com.exadel.pedrolima.ticketservice.service.TicketService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketReservationConsumer {

    private final TicketService ticketService;
    private final TicketConfirmationProducer confirmationProducer;

    public TicketReservationConsumer(TicketService ticketService, TicketConfirmationProducer confirmationProducer) {
        this.ticketService = ticketService;
        this.confirmationProducer = confirmationProducer;
    }

    @KafkaListener(topics = "ticketReservation", groupId = "ticket-service-group")
    public void consumeReservationRequest(TicketReservationEvent event) {
        System.out.println("DEBUG: Chegou no Listener? " + event);

        try{
            ticketService.createTicketFromEvent(event);

            TicketConfirmationEvent confirmation = new TicketConfirmationEvent();
            confirmation.setUserId(event.getUserId());
            confirmation.setSessionId(event.getSessionId());
            confirmation.setSeatNumber(event.getSeatNumber());
            confirmation.setConfirmed(true);
            confirmation.setMessage("Reservation confirmed successfully!");

            confirmationProducer.sendConfirmation(confirmation);
        } catch (Exception e){
            TicketConfirmationEvent rejection = new TicketConfirmationEvent();
            rejection.setUserId(event.getUserId());
            rejection.setSessionId(event.getSessionId());
            rejection.setSeatNumber(event.getSeatNumber());
            rejection.setConfirmed(false);
            rejection.setMessage("Booking failed: " + e.getMessage());
            e.printStackTrace();

            confirmationProducer.sendRejection(rejection);
        }
    }
}
