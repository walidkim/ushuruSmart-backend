package com.emtech.ushurusmart.transactions.entity;

import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double unitPrice;
    private String unitOfMeasure;
    @Column(nullable = false)
    private Boolean taxExempted;
    private Integer quantity;
    private String description;
    private String type;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Transaction> transactions= new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonBackReference
    private Owner owner;
}
