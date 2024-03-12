package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.Dtos.ResContructor;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerActionsController {
    @Autowired
    private OwnerService ownerService;

    private AssistantService assistantService;

    @PostMapping(value = "/update-details")
    public ResponseEntity<?> updatedDetails(@RequestBody Owner newOwner) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        owner.setPassword(newOwner.getPassword());
        owner.setEmail(newOwner.getEmail());
        owner.setName(newOwner.getName());
        owner.setPhoneNumber(newOwner.getPhoneNumber());

        ownerService.save(owner);

        ResContructor res = new ResContructor();
        res.setMessage("Your details have been updated successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping(value = "/add-assistant")
    public ResponseEntity<?> createTenant(@RequestBody Assistant assistant) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);
        assistant.setOwner(owner);
        assistantService.save(assistant);

        ResContructor res = new ResContructor();
        res.setMessage("Assistant added successfully!");

        return ResponseEntity.status(HttpStatus.CREATED).body(res)
                ;
    }

    @DeleteMapping(value = "/delete-assistant")
    public ResponseEntity<?> createAssistant(@NotNull @RequestParam(name = "assistant_id", required = true) long assistantId) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        ResContructor res = new ResContructor();

        Assistant assistant = assistantService.findById(assistantId);
        if(!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())){
            res.setMessage("You are not authorized to delete this assistant ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        res.setMessage("Assistant added successfully!");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping(value = "/update-assistant")
    public ResponseEntity<?> updateAssistant(@NotNull @RequestParam(name = "assistant_id", required = true) long assistantId) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        ResContructor res = new ResContructor();

        Assistant assistant = assistantService.findById(assistantId);
        if(!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())){
            res.setMessage("You are not authorized to update this assistant ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        assistantService.save(assistant);
        res.setMessage("Assistant "+ assistant.getName()+" updated successfully.");

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


}
