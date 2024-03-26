package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.Dtos.ResConstructor;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/owner")
@Tag(name="OwnerActions")
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
        owner.setBusinessKraPin(newOwner.getBusinessKraPin());
        owner.setBusinessOwnerKraPin(newOwner.getBusinessOwnerKraPin());

        ownerService.save(owner);

        ResConstructor res = new  ResConstructor();
        res.setMessage("Your details have been updated successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @PostMapping(value = "/add-assistant")
    public ResponseEntity<?> createAssistant(@RequestBody Assistant assistant) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);
        assistant.setOwner(owner);
        assistantService.save(assistant);

        ResConstructor res = new  ResConstructor();
        res.setMessage("Assistant added successfully!");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping(value = "/delete-assistant")
    public ResponseEntity<?> deleteAssistant(@NotNull @RequestParam(name = "assistant_id", required = true) long assistantId) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        ResConstructor res = new ResConstructor();

        Assistant assistant = assistantService.findById(assistantId);
        if(!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())){
            res.setMessage("You are not authorized to delete this assistant ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        assistantService.deleteById(assistantId);

        res.setMessage("Assistant deleted successfully!");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping(value = "/update-assistant")
    public ResponseEntity<?> updateAssistant(@NotNull @RequestParam(name = "assistant_id", required = true) long assistantId) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        ResConstructor res = new  ResConstructor();

        Assistant assistant = assistantService.findById(assistantId);
        if(!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())){
            res.setMessage("You are not authorized to update this assistant ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        assistantService.save(assistant);
        res.setMessage("Assistant "+ assistant.getName()+" updated successfully.");

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
//    @GetMapping(value = "/all-Assistants")
//    public ResponseEntity<List<Assistant>> getAllAssistants(@NotNull @RequestParam(name = "admin_id", required = true)long adminId) {
//        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
//        Owner owner = ownerService.findByEmail(ownerEmail);
//        ResContructor res = new ResContructor();
//
//        return ("");
}
