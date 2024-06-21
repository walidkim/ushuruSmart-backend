package com.emtech.ushurusmart.usermanagement.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import ch.qos.logback.core.model.Model;
import com.emtech.ushurusmart.usermanagement.Dtos.controller.ResetPasswordRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.controller.UpdateProfileDto;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.AssistantDto;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.BranchDto;
import com.emtech.ushurusmart.usermanagement.TrackingEntity.LoginLogoutListener;
import com.emtech.ushurusmart.usermanagement.factory.EntityFactory;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.BranchService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.service.resetpassword.ResetPasswordService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.emtech.ushurusmart.utils.EmailService;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;

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
    @Autowired

    private ResetPasswordService resetPasswordService;
    @Autowired
    private LoginLogoutListener loginLogoutListener;

    @PostMapping(value = "/update-details")
    public ResponseEntity<?> updatedDetails(@RequestBody Owner newOwner) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner = ownerService.findByEmail(ownerEmail);

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
            String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
            Owner owner = ownerService.findByEmail(ownerEmail);
            String password = assistantService.generateRandomPassword(8);
            String body = assistantService.createEmailBody(data.getName(), owner.getName(), data.getEmail(), password);
            emailService.sendEmail(data.getEmail(), "Welcome To Ushuru Smart", body);
            Assistant assistant = EntityFactory.createAssistant(data, password);
            assistant.setOwner(owner);
            res.setMessage("Assistant added successfully!");
            res.setData(assistantService.save(assistant));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            return responses.create500Response(e);
        }
    }

    @GetMapping(value = "/assistant-count")
    public ResponseEntity<?> assistantsCount() {
        ResContructor res = new ResContructor();
        try {
            String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
            Owner owner = ownerService.findByEmail(ownerEmail);
            res.setMessage("Assistant count fetched successfully!");
            res.setData(owner.getAssistants().size());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            return responses.create500Response(e);
        }
    }

    @DeleteMapping(value = "/delete-assistant")
    public ResponseEntity<?> deleteAssistant(@RequestParam(name = "assistant_id", required = true) long assistantId) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner = ownerService.findByEmail(ownerEmail);

        ResContructor res = new ResContructor();

        Assistant assistant = assistantService.findById(assistantId);
        if (!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())) {
            res.setMessage("You are not authorized to delete this assistant ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        assistantService.deleteById(assistantId);

        res.setMessage("Assistant deleted successfully!");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping(value = "/update-assistant")
    public ResponseEntity<?> updateAssistant(
            @NotNull @RequestParam(name = "assistant_id", required = true) long assistantId,
            @RequestBody Assistant newAssistant) {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner = ownerService.findByEmail(ownerEmail);

        ResContructor res = new ResContructor();

        Assistant assistant = assistantService.findById(assistantId);
        // if(!Objects.equals(owner.getEmail(), assistant.getOwner().getEmail())){
        // res.setMessage("You are not authorized to update this assistant ");
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        // }
        assistant.setEmail(newAssistant.getEmail());
        assistant.setName(newAssistant.getName());
        assistant.setPassword(newAssistant.getPassword());
        assistant.setBranch(newAssistant.getBranch());
        assistant.setPhoneNumber(newAssistant.getPhoneNumber());
        assistant = assistantService.save(assistant);
        res.setMessage("Assistant " + assistant.getName() + " updated successfully.");
        res.setData(assistant);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping(value = "/assistants")
    public ResponseEntity<?> getAllAssistants() {
        String ownerEmail = AuthUtils.getCurrentlyLoggedInPerson(

        );
        Owner owner = ownerService.findByEmail(ownerEmail);
        ResContructor res = new ResContructor();
        res.setData(owner.getAssistants());
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @Autowired
    public OwnerActionsController(LoginLogoutListener loginLogoutListener) {
        this.loginLogoutListener = loginLogoutListener;
    }

    @GetMapping("/owner/assistants")
    public ResponseEntity<Integer> getLoggedInAssistantsCount() {
        int count = loginLogoutListener.getLoggedInAssistantsCount();
        return ResponseEntity.ok(count);
    }

    @Autowired
    private BranchService branchService;

    public OwnerActionsController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/count-branches")
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        List<BranchDto> branches = branchService.getAllBranches();
        return new ResponseEntity<>(branches, HttpStatus.OK);
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable("id") Long id) {
        return branchService.getBranchById(id)
                .map(branchDto -> new ResponseEntity<>(branchDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create-branches")
    public ResponseEntity<BranchDto> createBranch(@RequestBody BranchDto branchDto) {
        BranchDto createdBranch = branchService.createBranch(branchDto);
        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    @PutMapping("/update-branches/{id}")
    public ResponseEntity<BranchDto> updateBranch(@PathVariable("id") Long id, @RequestBody BranchDto branchDto) {
        BranchDto updatedBranch = branchService.updateBranch(id, branchDto);
        return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
    }

    @DeleteMapping("/delete-branches/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("id") Long id) {
        branchService.deleteBranch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetOwnerPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        resetPasswordService.changePassword(email, resetPasswordRequest, "Owner");

        return ResponseEntity.status(HttpStatus.OK).body("Password successfully changed for owner.");
    }

    @GetMapping("/assign-branch-form")
    public String showAssignBranchForm(Model model) {
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("assistants", assistantService.getAllAssistants());
        return "assign-branch-form";
    }

    @PostMapping("/assign-branch")
    public String assignBranchToAssistant(@RequestParam Long branchId, @RequestParam Long assistantId) {
        Optional<BranchDto> branch = branchService.getBranchById(branchId);
        Assistant assistant = assistantService.getAssistantById(assistantId);
        if (branch.isPresent() && assistant != null) {
            assistant.setBranch(String.valueOf(branch));
            assistantService.saveAssistant(assistant);
        }
        return "redirect:/assign-branch";
    }

    @PutMapping(value = "/update_profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("profilePhoto") MultipartFile profilePhoto) {
        try {
            byte[] photoBytes = profilePhoto.getBytes();

            UpdateProfileDto request = new UpdateProfileDto();
            request.setName(name);
            request.setEmail(email);
            request.setProfilePhoto(photoBytes);

            ownerService.updateProfile(request);
            return ResponseEntity.ok("profile Updated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the profile" + e.getMessage());
        }
    }

}
