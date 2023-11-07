package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.controller.dto.*;
import com.ali.mirsalari.wrench.controller.mapper.ExpertResponseMapper;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.service.ExpertService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/expert")
public class ExpertController {
    private final ExpertResponseMapper expertResponseMapper;
    private final ExpertService expertService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"","/"})
    public List<ExpertResponse> getExperts() {
        List<Expert> experts = expertService.findAll();
        return experts.stream().map(expertResponseMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registerExpert(@Valid RegisterExpertRequest request) {
        byte[] imageData;
        try {
            imageData = request.image().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Expert expert = expertService.save(request.firstName(), request.lastName(), request.email(), request.password(), imageData);
        return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @PreAuthorize("hasRole('EXPERT')")
    @PutMapping(path = "/updateExpert")
    public ResponseEntity<?> updateExpert(
            @Valid RegisterExpertRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        byte[] imageData;
        try {
            imageData = request.image().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Expert expert = expertService.update(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                imageData,
                userDetails);
        return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{expertId}")
    public ResponseEntity<?> deleteExpert(@PathVariable("expertId") Long expertId) {
        expertService.remove(expertId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<ExpertResponse> findById(@PathVariable Long id) {
        Optional<Expert> expert = expertService.findById(id);
        return expert.map(expertResponseMapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<ExpertResponse> findByEmail(@PathVariable String email) {
        Optional<Expert> expert = expertService.findByEmail(email);
        return expert.map(expertResponseMapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approveExpert/{id}")
    public ResponseEntity<String> approveExpert(@PathVariable Long id) {
        expertService.approveExpert(id);
        return ResponseEntity.ok("Expert with ID " + id + " has been approved.");
    }

    @PreAuthorize("hasRole('EXPERT')")
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        Expert expert =
                expertService
                        .changePassword(
                                request
                                        .newPassword(),
                                request
                                        .oldPassword(),
                                userDetails.getUsername());
        return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    @PostMapping("/addSkill")
    public ResponseEntity<?> addSkillToExpert(@Valid @RequestBody SkillRequest request) {
        expertService.addSkill(request.skillId(), request.expertId());
        return ResponseEntity.ok("Skill is added!");
    }
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    @DeleteMapping("/removeSkill")
    public ResponseEntity<?> removeSkillToExpert(@Valid @RequestBody SkillRequest request) {
        expertService.removeSkill(request.skillId(), request.expertId());
        return ResponseEntity.ok("Skill is removed!");
    }
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    @PostMapping("/savePhotoToFile")
    public ResponseEntity<String> savePhotoToFile(@Valid @RequestBody ImageRequest imageRequest) {
        expertService.retrieveAndSavePhotoToFile(imageRequest.expertId(), imageRequest.filePath());
        return ResponseEntity.ok("Photo has been saved to " + imageRequest.filePath());
    }
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    @PostMapping("sendActivationLink/{email}")
    public ResponseEntity<String> sendActivationLink(
            @Valid @Email @PathVariable("email") String email){
        expertService.sendActivationLink(email);
        return ResponseEntity.ok("Activation link is sent!");
    }

    @GetMapping("approveEmail/{email}/{token}")
    public ResponseEntity<String> approveEmail(
            @Valid @Email @PathVariable("email") String email,
            @PathVariable("token") String token){
        expertService.approveEmail(email, token);
        return ResponseEntity.ok("Email has been approved!");
    }
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    @GetMapping("/getRegisterDate/{id}")
    public ResponseEntity<String> getRegisterDate(@PathVariable Long id) {
        Optional<Expert> expertOptional = expertService.findById(id);

        return expertOptional.map(expert -> {
            Instant registerTime = expert.getRegisterTime();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(registerTime, zoneId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zoneId);
            String formattedTime = formatter.format(zonedDateTime);

            return ResponseEntity.ok(formattedTime);
        }).orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('EXPERT')")
    @GetMapping("/getCreditAmount")
    public ResponseEntity<?> getCreditAmount(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<Expert> expertOptional = expertService.findByEmail(userDetails.getUsername());

        return expertOptional.map(expert -> ResponseEntity.ok(expert.getCredit()))
                .orElse(ResponseEntity.notFound().build());
    }
}
