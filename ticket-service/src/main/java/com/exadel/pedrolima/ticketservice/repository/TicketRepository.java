package com.exadel.pedrolima.ticketservice.repository;

import com.exadel.pedrolima.ticketservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Override
    Optional<Ticket> findById(Long id);

    List<Ticket> findByUserId(Long userId);

    Optional<Ticket> findBySeatNumber(String seatNumber);
}
