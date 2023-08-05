package com.example.cardorderingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCard {
    private long id;
    private String name;
    private String cardType;
    private String orderStatus;
    private boolean createdByManager;

}
