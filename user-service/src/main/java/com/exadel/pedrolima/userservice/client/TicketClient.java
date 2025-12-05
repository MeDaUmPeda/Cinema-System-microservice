package com.exadel.pedrolima.userservice.client;

import com.exadel.pedrolima.userservice.dto.TicketResponse;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@EnableFeignClients
@FeignClient(name = "ticket-service", url = "http://ticket-service:8082")
public interface TicketClient {

    @GetMapping("/api/user/{userId}")
    List<TicketResponse> getTicketByUser(@PathVariable Long userId);
}
