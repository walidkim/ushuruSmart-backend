package com.emtech.ushurusmart.payments.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "Eslip")

public class Eslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eslipNumber;
    private LocalDateTime createdDateTime;
    private int amount;

    // Constructors, getters, and setters
    public Eslip() {
        this.createdDateTime = LocalDateTime.now();
    }

    public Eslip(String eslipNumber, int amount) {
        this();
        this.eslipNumber = eslipNumber;
        this.amount = amount;
    }

}
