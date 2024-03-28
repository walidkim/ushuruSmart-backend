package com.emtech.ushurusmart.usermanagement.factory;

import com.emtech.ushurusmart.usermanagement.Dtos.OwnerDto;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.AssistantDto;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import org.springframework.web.filter.OncePerRequestFilter;

public class EntityFactory {
    public static Owner createOwner(OwnerDto data){
        Owner owner= new Owner();
        owner.setRole(Role.owner);
        owner.setPhoneNumber(data.getPhoneNumber());
        owner.setName(data.getName());
        owner.setEmail(data.getEmail());
        owner.setBusinessKRAPin((data.getBusinessKRAPin()));
        owner.setBusinessOwnerKRAPin(data.getBusinessOwnerKRAPin());
        owner.setPassword(data.getPassword());
        return owner;
    }

    public static Assistant createAssistant(AssistantDto data){
        Assistant assistant= new Assistant();
        assistant.setRole(Role.assistant);
        assistant.setPhoneNumber(data.getPhoneNumber());
        assistant.setName(data.getName());
        assistant.setEmail(data.getEmail());
        assistant.setBranch((data.getBranch()));
        assistant.setPassword(data.getPassword());
        return assistant;
    }
}
