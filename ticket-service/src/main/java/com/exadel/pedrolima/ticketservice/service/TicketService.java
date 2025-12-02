package com.exadel.pedrolima.ticketservice.service;

import com.exadel.pedrolima.ticketservice.client.SessionClient;
import com.exadel.pedrolima.ticketservice.client.UserClient;
import com.exadel.pedrolima.ticketservice.dto.CreateTicketRequest;
import com.exadel.pedrolima.ticketservice.dto.SessionResponse;
import com.exadel.pedrolima.ticketservice.dto.TicketResponse;
import com.exadel.pedrolima.ticketservice.dto.UserResponse;
import com.exadel.pedrolima.ticketservice.entity.Ticket;
import com.exadel.pedrolima.ticketservice.entity.enums.TicketStatus;
import com.exadel.pedrolima.ticketservice.event.TicketReservationEvent;
import com.exadel.pedrolima.ticketservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserClient userClient;
    private final SessionClient sessionClient;

    public TicketService(TicketRepository ticketRepository, UserClient userClient,  SessionClient sessionClient) {
        this.ticketRepository = ticketRepository;
        this.userClient = userClient;
        this.sessionClient = sessionClient;
    }

    private TicketResponse convertToDto(Ticket ticket) {
        return new TicketResponse(ticket.getId(), ticket.getSeatNumber(), ticket.getStatus(), ticket.getUserId(), ticket.getSessionId());
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + "not found"));
        return convertToDto(ticket);
    }

    public List<TicketResponse> getTicketsByUserId(Long userId) {

        userClient.getUserById(userId);

        List<Ticket> tickets = ticketRepository.findByUserId(userId);

        return tickets.stream()
                .map(this::convertToDto)
                .toList();
    }

    public TicketResponse createTicket(CreateTicketRequest request) {
        UserResponse user = userClient.getUserById(request.getUserId());
        if(user == null) {
            throw new ResourceNotFoundException("User not found with id " + request.getUserId());
        }

        Ticket ticket = Ticket.builder()
                .seatNumber(request.getSeatNumber())
                .sessionId(request.getSessionId())
                .userId(request.getUserId())
                .status(request.getStatus() != null ? request.getStatus() : TicketStatus.RESERVED)
                .build();

        return convertToDto(ticketRepository.save(ticket));
    }

    public TicketResponse updateTicket(Long id, CreateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setStatus(request.getStatus());

        return convertToDto(ticketRepository.save(ticket));
    }

    public void deleteTicketById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }



    public void createTicketFromEvent(TicketReservationEvent event){
        System.out.println("Creating ticket for user " + event.getUserId() + " and session " + event.getSessionId());

        UserResponse user = userClient.getUserById(event.getUserId());
        if(user == null) {
            throw new ResourceNotFoundException("User not found with id " + event.getUserId());
        }

        SessionResponse session = sessionClient.getSessionById(event.getSessionId());
        if(session == null) {
            throw new ResourceNotFoundException("Session not found with id " + event.getSessionId());
        }

        if(session.getAvailableSeats() == null || session.getAvailableSeats() == 0) {
            throw new ResourceNotFoundException("No available seats in this session " + event.getSessionId());
        }

        Ticket ticket = new Ticket();
        ticket.setSeatNumber(String.valueOf(event.getSeatNumber()));
        ticket.setStatus(TicketStatus.RESERVED);
        ticket.setUserId(event.getUserId());
        ticket.setSessionId(event.getSessionId());

        sessionClient.decrementSeatCount(session.getId());

        Ticket saved = ticketRepository.save(ticket);
        System.out.println("The ticket has been created successfully: ID " + saved.getId());
    }

}
