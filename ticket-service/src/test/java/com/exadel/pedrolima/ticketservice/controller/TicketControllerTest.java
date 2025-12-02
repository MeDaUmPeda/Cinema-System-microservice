package com.exadel.pedrolima.ticketservice.controller;

import com.exadel.pedrolima.ticketservice.dto.CreateTicketRequest;
import com.exadel.pedrolima.ticketservice.dto.TicketResponse;
import com.exadel.pedrolima.ticketservice.entity.enums.TicketStatus;
import com.exadel.pedrolima.ticketservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.ticketservice.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @Test
    void testGetAllTickets() throws Exception {
        when(ticketService.getAllTickets())
                .thenReturn(List.of
                        (new TicketResponse(1L, "A1", TicketStatus.RESERVED, 1L, 1L))
                );

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("A1"));

    }

    @Test
    void testGetTicketByIdSuccess() throws Exception {
        when(ticketService.getTicketById(1L))
                .thenReturn(new TicketResponse(1L, "A1", TicketStatus.RESERVED, 1L, 1L));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value("A1"));
    }

    @Test
    void testGetTicketByIdNotFound() throws Exception {
        when(ticketService.getTicketById(1L))
                .thenThrow(new ResourceNotFoundException("Ticket not found"));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTicketSuccess() throws Exception {
        CreateTicketRequest createTicketRequest = new CreateTicketRequest("A1", 1L, 1L, TicketStatus.RESERVED);
        TicketResponse ticketResponse = new TicketResponse(1L, "A1", TicketStatus.RESERVED, 1L, 1L);

        when(ticketService.createTicket(any(CreateTicketRequest.class))).thenReturn(ticketResponse);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTicketRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber").value("A1"));
    }

    @Test
    void testUpdateTicketSuccess() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest("B2", 1L,  1L,TicketStatus.CANCELED);
        TicketResponse response = new TicketResponse(1L, "B2", TicketStatus.CANCELED, 1L, 1L);

        when(ticketService.updateTicket(any(Long.class), any(CreateTicketRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value("B2"));
    }


    @Test
    void testDeleteTicketSuccess() throws Exception {
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTicketNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Ticket not found"))
                .when(ticketService).deleteTicketById(10L);

        mockMvc.perform(delete("/api/tickets/10"))
                .andExpect(status().isNotFound());
    }

}