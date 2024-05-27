package com.emtech.ushurusmart.usermanagement.TrackingEntity;

import org.apache.commons.logging.impl.WeakHashtable;
import org.apache.poi.hssf.model.WorkbookRecordList;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
@Component
public class LoginLogoutListener  {
    private final ConcurrentHashMap.KeySetView<String, Boolean> loggedInAssistants = ConcurrentHashMap.newKeySet();

    @EventListener
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            String username = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
            if (event.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ASSISTANT"))) {
                loggedInAssistants.add(username);
            }
        } else if (event instanceof LogoutSuccessEvent) {
            String username = ((UserDetails) ((LogoutSuccessEvent) event).getAuthentication().getPrincipal()).getUsername();
            loggedInAssistants.remove(username);
        }
    }

    public int getLoggedInAssistantsCount() {
        return loggedInAssistants.size();
    }
}
