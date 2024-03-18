package com.emtech.ushurusmart.etrModule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions", schema="ushuru_smart")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable=false)
    private Long id;
    @Column(nullable=false)
    private double amount;
    @Column(nullable=false)
    private String buyerPin;
    @CreationTimestamp
    private LocalDateTime dateCreated;

}
