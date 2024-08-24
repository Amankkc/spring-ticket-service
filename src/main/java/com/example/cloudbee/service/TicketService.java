package com.example.cloudbee.service;
import com.example.cloudbee.Model.Ticket;
import com.example.cloudbee.Model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketService {

    private final Map<String, Ticket> ticketRepository = new ConcurrentHashMap<>();
    private final Map<String, String> seatAllocation = new ConcurrentHashMap<>();
    private final AtomicInteger seatCounter = new AtomicInteger(1);

    public Ticket purchaseTicket(User user) {
        log.info("Purchasing ticket for user: {}", user.getEmail());
        String seatNumber = allocateSeat();
        Ticket ticket = Ticket.TicketFactory.createTicket("London", "France", user, 20, seatNumber);
        ticketRepository.put(user.getEmail(), ticket);
        seatAllocation.put(seatNumber, user.getEmail());
        return ticket;
    }

    public Ticket getTicket(String email) {
        log.info("Retrieving ticket for email: {}", email);
        return ticketRepository.get(email);
    }

    public Map<String, String> viewSeatsBySection(String section, int page, int size) {
        log.info("Viewing seats for section: {} with page: {} and size: {}", section, page, size);
        return seatAllocation.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(section))
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void removeUser(String email) {
        log.info("Removing user with email: {}", email);
        Ticket ticket = ticketRepository.remove(email);
        if (ticket != null) {
            seatAllocation.remove(ticket.getSeatNumber());
        }
    }

    public void modifySeat(String email, String newSeat) {
        log.info("Modifying seat for email: {} to new seat: {}", email, newSeat);
        Ticket ticket = ticketRepository.get(email);
        if (ticket != null) {
            seatAllocation.remove(ticket.getSeatNumber());
            ticket.setSeatNumber(newSeat);
            seatAllocation.put(newSeat, email);
        }
    }

    private String allocateSeat() {
        String section = seatCounter.get() % 2 == 0 ? "B" : "A";
        return section + seatCounter.getAndIncrement();
    }

    public boolean userAlreadyBooked(String email) {
        return ticketRepository.containsKey(email);
    }
}
