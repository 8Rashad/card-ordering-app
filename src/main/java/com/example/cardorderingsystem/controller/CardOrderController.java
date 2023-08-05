package com.example.cardorderingsystem.controller;

import com.example.cardorderingsystem.model.Ticket;
import com.example.cardorderingsystem.model.UserCard;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
public class CardOrderController {

    private List<UserCard> orders = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private AtomicLong orderIdGenerator = new AtomicLong(1);
    private AtomicLong ticketIdGenerator = new AtomicLong(1);
    @PostMapping("/order")
    public ResponseEntity<String> orderCard(@RequestBody UserCard userCard){
        userCard.setId(orderIdGenerator.getAndIncrement());
        userCard.setOrderStatus("Pending");
        orders.add(userCard);

        userCard.setCreatedByManager(true);

        return ResponseEntity.ok("Card ordered successfully");
    }

    @GetMapping("/orders")
    public ResponseEntity<List<UserCard>> getAllOrders(){
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable long id, @RequestBody String orderStatus) {
        UserCard userCard = orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
        if (userCard != null) {
            userCard.setOrderStatus(orderStatus);
            return ResponseEntity.ok("Order status updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/tickets")
    public ResponseEntity<String> createTicket(@RequestBody Ticket ticket) {
        UserCard order = orders.stream()
                .filter(o -> o.getId() == ticket.getOrderId())
                .findFirst()
                .orElse(null);

        if (order != null) {
            ticket.setId(ticketIdGenerator.getAndIncrement());
            tickets.add(ticket);
            return ResponseEntity.ok("Ticket created successfully!");
        } else {
            return ResponseEntity.badRequest().body("Invalid orderId. No order found.");
        }
    }

    // New endpoint to get all open tickets
    @GetMapping("/tickets/open")
    public ResponseEntity<List<Ticket>> getAllOpenTickets() {
        List<Ticket> openTickets = tickets.stream()
                .filter(ticket -> !ticket.isReady())
                .collect(Collectors.toList());

        return ResponseEntity.ok(openTickets);
    }

    @PutMapping("/tickets/{id}/status")
    public ResponseEntity<String> updateTicketStatus(@PathVariable long id, @RequestParam boolean ready) {
        Ticket ticket = tickets.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (ticket != null) {
            UserCard order = orders.stream()
                    .filter(o -> o.getId() == ticket.getOrderId())
                    .findFirst()
                    .orElse(null);

            if (order != null && order.isCreatedByManager()) {
                ticket.setReady(ready);
                return ResponseEntity.ok("Ticket status updated successfully!");
            } else {
                return ResponseEntity.badRequest().body("Only managers can update ticket status.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
