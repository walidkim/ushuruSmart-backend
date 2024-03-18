package com.emtech.ushurusmart.transactions.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String buyerPin;
    private Boolean taxExemption;
    private String invoiceNumber;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    // assume sale transaction has only one product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;
    private Integer productQuantity;
    private Double productUnitPrice;
    private String productName;
}
