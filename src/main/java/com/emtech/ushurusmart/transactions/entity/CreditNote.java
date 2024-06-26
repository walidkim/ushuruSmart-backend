package com.emtech.ushurusmart.transactions.entity;




import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditNote {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String invoiceNumber;
    private String buyerPin;
    @ManyToMany
    private List<Product> products= new ArrayList<>();
    private Double productAmount;
    private String productName;
    private Integer productQuantity;
}
