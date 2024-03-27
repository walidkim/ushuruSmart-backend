package com.emtech.ushurusmart.usermanagement.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.emtech.ushurusmart.usermanagement.model.Permission.*;

@RequiredArgsConstructor
public enum Role {
    admin(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    ADMIN_READ,
                    ADMIN_UPDATE

            )
    ),
    owner(
            Set.of(
                    OWNER_CREATE,
                    OWNER_DELETE,
                    OWNER_UPDATE,
                    OWNER_READ
            )
    ),
    assistant(
            Set.of(
                    ASSISTANT_CREATE,
                    ASSISTANT_DELETE,
                    ASSISTANT_READ,
                    ASSISTANT_UPDATE

            )

    )

;
    @Getter
    private final Set<Permission> permission;


    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermission()
                .stream()
                .map(permission1 -> new SimpleGrantedAuthority(permission1.getPermission()))
                .collect(Collectors.toList());
        return authorities;
    }

    }
