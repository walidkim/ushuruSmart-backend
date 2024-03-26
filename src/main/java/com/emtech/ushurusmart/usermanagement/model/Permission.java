package com.emtech.ushurusmart.usermanagement.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),

    ADMIN_DELETE("admin:delete"),

    ADMIN_READ("admin:read"),

    ADMIN_UPDATE("admin:update"),

    OWNER_CREATE("owner:create"),

    OWNER_DELETE("owner:delete"),

    OWNER_READ("owner:read"),

    OWNER_UPDATE("owner:update"),

    ASSISTANT_CREATE("assistant:create"),

    ASSISTANT_DELETE("assistant:delete"),

    ASSISTANT_READ("assistant:read"),

    ASSISTANT_UPDATE("assistant:update"),

    ;

    @Getter
    private final String permission;
}
