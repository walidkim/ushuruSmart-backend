package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
        @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
        private Long user_id;
        @Column(nullable = false)
        private String username;
        @Column(nullable = false)
        private String password;
        @Column(nullable =false)
        private Integer phonenumber;
        @Column(nullable = false)
        private String branch;

        private boolean verified;
        @ManyToOne
        @JoinColumn(name = "admin_id", nullable = false)
        private Admin admin;

}
