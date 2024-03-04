package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseAuth {
        @Column(nullable = false)
        private Integer phonenumber;
        @Column(nullable = false)
        private String branch;

        private boolean verified;
        @ManyToOne
        @JoinColumn(name = "admin_id", nullable = false)
        private Admin admin;
}
