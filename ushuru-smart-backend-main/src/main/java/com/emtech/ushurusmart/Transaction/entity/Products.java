package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
<<<<<<< HEAD
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
>>>>>>> b12f94f (changes)
=======
import lombok.*;
>>>>>>> 4476ce9 (Credit Note)
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
<<<<<<< HEAD
<<<<<<< HEAD
@Getter
@Setter
=======
>>>>>>> b12f94f (changes)
=======
@Getter
@Setter
>>>>>>> 4476ce9 (Credit Note)
@Table(name = "products", schema="ushuru_smart")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    @Column(nullable=false)
<<<<<<< HEAD
<<<<<<< HEAD
    private double unitPrice;
=======
    private int unitPrice;
>>>>>>> b12f94f (changes)
=======
    private double unitPrice;
>>>>>>> 4476ce9 (Credit Note)
    private String unitOfMeasure;
    @Column(nullable=false)
    private Boolean taxExempted;
    private int quantity;
<<<<<<< HEAD
<<<<<<< HEAD
    private String type;
=======
>>>>>>> b12f94f (changes)
=======
    private String type;
>>>>>>> 4476ce9 (Credit Note)
    private String description;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;
}
