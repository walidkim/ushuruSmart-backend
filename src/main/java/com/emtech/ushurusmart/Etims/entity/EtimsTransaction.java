package com.emtech.ushurusmart.Etims.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Etimstransactions")
public class EtimsTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private String ownerPin;
    @Column(nullable = false)
    private String buyerPin;

    @Column(nullable = false)
    private boolean taxable;
    private String invoiceNumber;

    private String etimsNumber;
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column
    private String name;

}
