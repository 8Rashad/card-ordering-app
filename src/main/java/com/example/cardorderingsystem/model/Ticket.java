package com.example.cardorderingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private long id;
    private String subject;
    private String description;
    private long orderId;
    private boolean ready;
}
