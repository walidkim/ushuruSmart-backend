package com.emtech.ushurusmart.usermanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assistants")

public class Assistant extends BaseAuth {
        private String phoneNumber;

        private String branch;

        private boolean verified;
//        @ManyToOne
//        @JoinColumn(name = "owner_id", nullable = false)
//        private Owner owner;
}
