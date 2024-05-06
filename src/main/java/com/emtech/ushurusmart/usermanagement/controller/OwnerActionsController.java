package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.Dtos.entity.AssistantDto;
import com.emtech.ushurusmart.usermanagement.factory.EntityFactory;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.emtech.ushurusmart.utils.EmailService;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import org.jetbrains.annotations.NotNull;
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


    @Autowired
    private EmailService emailService;

    @Autowired
    private AssistantService assistantService;


    @Autowired
    private Responses responses;

    @PostMapping(value = "/update-details")
    public ResponseEntity<?> updatedDetails(@RequestBody Owner newOwner) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        owner.setPassword(newOwner.getPassword());
        owner.setEmail(newOwner.getEmail());
        owner.setName(newOwner.getName());
        owner.setPhoneNumber(newOwner.getPhoneNumber());
        owner.setBusinessKRAPin(newOwner.getBusinessKRAPin());
        owner.setKRAPin(newOwner.getKRAPin());

        ownerService.save(owner);

        ResContructor res = new ResContructor();
        res.setMessage("Your details have been updated successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @PostMapping(value = "/create-assistant")
    public ResponseEntity<?> createAssistant(@RequestBody AssistantDto data) {
        ResContructor res = new ResContructor();
        try {
            String ownerEmail= AuthUtils.getCurrentlyLoggedInPerson();
            Owner owner = ownerService.findByEmail(ownerEmail);
            String password= assistantService.generateRandomPassword(8);
            String body= assistantService.createEmailBody(data.getName(),owner.getName(),data.getEmail(),password);
            emailService.sendEmail(data.getEmail(), "Welcome To Ushuru Smart", body);
            Assistant assistant= EntityFactory.createAssistant(data, password);
            assistant.setOwner(owner);
            res.setMessage("Assistant added successfully!");
            res.setData(assistantService.save(assistant));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }catch ( Exception e){
           return responses.create500Response(e);
        }
    }

    @DeleteMapping(value = "/delete-assistant")
    public ResponseEntity<?> deleteAssistant(@RequestParam(name = "assistant_id", required = true) long assistantId) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        ResContructor res = new ResContructor();

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
    public ResponseEntity<?> updateAssistant(@NotNull @RequestParam(name = "assistant_id", required = true) long assistantId,@RequestBody Assistant  newAssistant) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(ownerEmail);

        ResContructor res = new ResContructor();

        Assistant assistant = assistantService.findById(assistantId);
//        if(!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())){
//            res.setMessage("You are not authorized to update this assistant ");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
//        }
        assistant.setEmail(newAssistant.getEmail());
        assistant.setName(newAssistant.getName());
        assistant.setPassword(newAssistant.getPassword());
        assistant.setBranch(newAssistant.getBranch());
        assistant.setPhoneNumber(newAssistant.getPhoneNumber());
        assistant = assistantService.save(assistant);
        res.setMessage("Assistant "+ assistant.getName()+" updated successfully.");
        res.setData(assistant);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @GetMapping(value = "/assistants")
    public ResponseEntity<?> getAllAssistants() {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner = ownerService.findByEmail(ownerEmail);
        ResContructor res = new ResContructor();
        res.setData(owner.getAssistants());
        return ResponseEntity.status(HttpStatus.OK).body(res);


    }
}
