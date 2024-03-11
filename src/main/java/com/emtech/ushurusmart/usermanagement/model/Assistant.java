package com.emtech.ushurusmart.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assistants")

public class Assistant extends BaseAuth {
        private String phoneNumber;

        private String branch;

        private boolean verified;
        @ManyToOne
        @JoinColumn(name = "owner_id", nullable = false)
        private Owner owner;
}
