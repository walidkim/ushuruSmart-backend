package com.emtech.ushurusmart.etims.entity;

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
@Table(name = "etims-sales")
public class Sale {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private boolean taxable;
  private double tax;
  private String name;
  private double amount;
}
