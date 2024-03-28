package com.emtech.ushurusmart.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


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
        @JoinColumn( nullable = false)
        @JsonBackReference
        private Owner owner;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return getRole().getAuthorities();
        }

        @Override
        public String getUsername() {
                return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return false;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }
}
