package com.emtech.ushurusmart.usermanagement.TrackingEntity;

import com.emtech.ushurusmart.usermanagement.utils.SecurityUtils;
import org.apache.catalina.User;

import jakarta.persistence.PrePersist;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

import java.time.LocalDateTime;

public abstract class TrackingEntityListener {
    @PrePersist
    public void onPrePersist(Object entity) {
        if (entity instanceof TrackingEntity) {
            TrackingEntity trackingEntity = (TrackingEntity) entity;
            User currentUser = SecurityUtils.getCurrentUser();

            trackingEntity.setCreatedAt(LocalDateTime.now());
            if (currentUser != null) {
                trackingEntity.setAddedBy(currentUser);
            }
        }
    }

    public abstract void onApplicationEvent(AbstractAuthenticationEvent event);
}
