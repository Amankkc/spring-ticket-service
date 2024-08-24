package com.example.cloudbee.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {
    private String from;
    private String to;
    private User user;
    private double price;
    private String seatNumber;

    public static class TicketFactory {
        public static Ticket createTicket(String from, String to, User user, double price, String seatNumber) {
            return new Ticket(from, to, user, price, seatNumber);
        }
    }
}
