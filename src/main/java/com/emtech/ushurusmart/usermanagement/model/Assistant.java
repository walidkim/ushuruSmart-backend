package com.emtech.ushurusmart.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assistants")

public class Assistant extends BaseAuth implements Serializable {
        private String branch;
        @ManyToOne(fetch = FetchType.EAGER)
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

        private boolean loggedInStatus;
}
