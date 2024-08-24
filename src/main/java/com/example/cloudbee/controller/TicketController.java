package com.example.cloudbee.controller;
import com.example.cloudbee.Model.Ticket;
import com.example.cloudbee.Model.User;
import com.example.cloudbee.service.TicketService;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/tickets")
@Validated
@Slf4j
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RateLimiter rateLimiter;

    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@Valid @RequestBody User user) {
        if (!rateLimiter.tryAcquire()) {
            log.warn("Rate limit exceeded for purchasing ticket");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        if (ticketService.userAlreadyBooked(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Ticket ticket = ticketService.purchaseTicket(user);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Ticket> getTicket(@PathVariable String email) {
        if (!rateLimiter.tryAcquire()) {
            log.warn("Rate limit exceeded for retrieving ticket");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        Ticket ticket = ticketService.getTicket(email);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/seats")
    public ResponseEntity<Map<String, String>> viewSeats(@RequestParam String section,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        if (!rateLimiter.tryAcquire()) {
            log.warn("Rate limit exceeded for viewing seats");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        Map<String, String> seats = ticketService.viewSeatsBySection(section, page, size);
        return ResponseEntity.ok(seats);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removeUser(@PathVariable String email) {
        if (!rateLimiter.tryAcquire()) {
            log.warn("Rate limit exceeded for removing user");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        ticketService.removeUser(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modify-seat/{email}")
    public ResponseEntity<Void> modifySeat(@PathVariable String email, @RequestParam String newSeat) {
        if (!rateLimiter.tryAcquire()) {
            log.warn("Rate limit exceeded for modifying seat");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        ticketService.modifySeat(email, newSeat);
        return ResponseEntity.noContent().build();
    }
}

