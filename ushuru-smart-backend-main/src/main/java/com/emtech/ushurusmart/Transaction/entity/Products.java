package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "products", schema="ushuru_smart")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    @Column(nullable=false)
    private double unitPrice;
    private String unitOfMeasure;
    @Column(nullable=false)
    private Boolean taxExempted;
    private int quantity;
    private String type;
    private String description;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;
}
