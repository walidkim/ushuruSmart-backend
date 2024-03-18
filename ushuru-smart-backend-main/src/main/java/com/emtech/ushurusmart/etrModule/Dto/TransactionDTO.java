package com.emtech.ushurusmart.etrModule.Dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    @Id
    private Long id ;
    private double amount;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    private String buyerPin;
}
