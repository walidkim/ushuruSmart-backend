package com.emtech.ushurusmart.Transaction.Dto;

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
    private Long id;
    private double amount;
    private String buyerPin;
    @CreationTimestamp
    private LocalDateTime dateCreated;

}
