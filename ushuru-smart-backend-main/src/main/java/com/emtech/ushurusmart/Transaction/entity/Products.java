package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
>>>>>>> b12f94f (changes)
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
<<<<<<< HEAD
@Getter
@Setter
=======
>>>>>>> b12f94f (changes)
@Table(name = "products", schema="ushuru_smart")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    @Column(nullable=false)
<<<<<<< HEAD
    private double unitPrice;
=======
    private int unitPrice;
>>>>>>> b12f94f (changes)
    private String unitOfMeasure;
    @Column(nullable=false)
    private Boolean taxExempted;
    private int quantity;
<<<<<<< HEAD
    private String type;
=======
>>>>>>> b12f94f (changes)
    private String description;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;
}
