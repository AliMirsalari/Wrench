package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.controller.dto.*;
import com.ali.mirsalari.wrench.controller.mapper.ExpertResponseMapper;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.service.ExpertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/expert")
public class ExpertController {
    private final ExpertResponseMapper expertResponseMapper;
    private final ExpertService expertService;


    @GetMapping
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

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateExpert(
            @PathVariable("id") Long id,
            @Valid RegisterExpertRequest request) {

        byte[] imageData;
        try {
            imageData = request.image().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Expert expert = expertService.update(id, request.firstName(), request.lastName(), request.email(), request.password(), imageData);
        return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @DeleteMapping(path = "{expertId}")
    public ResponseEntity<?> deleteExpert(@PathVariable("expertId") Long expertId) {
        expertService.remove(expertId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ExpertResponse> findById(@PathVariable Long id) {
        Optional<Expert> expert = expertService.findById(id);
        return expert.map(expertResponseMapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<ExpertResponse> findByEmail(@PathVariable String email) {
        Optional<Expert> expert = expertService.findByEmail(email);
        return expert.map(expertResponseMapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/approveExpert/{id}")
    public ResponseEntity<String> approveExpert(@PathVariable Long id) {
        expertService.approveExpert(id);
        return ResponseEntity.ok("Expert with ID " + id + " has been approved.");
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @Valid @RequestBody ChangePasswordRequest request) {
        Expert expert = expertService.changePassword(request.newPassword(), request.oldPassword(), userId);
        return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @PostMapping("/addSkill")
    public ResponseEntity<?> addSkillToExpert(@Valid @RequestBody SkillRequest request) {
        expertService.addSkill(request.skillId(), request.expertId());
        return ResponseEntity.ok("Skill is added!");
    }

    @PostMapping("/removeSkill")
    public ResponseEntity<?> removeSkillToExpert(@Valid @RequestBody SkillRequest request) {
        expertService.removeSkill(request.skillId(), request.expertId());
        return ResponseEntity.ok("Skill is removed!");
    }

    @PostMapping("/savePhotoToFile")
    public ResponseEntity<String> savePhotoToFile(@Valid @RequestBody ImageRequest imageRequest) {
        expertService.retrieveAndSavePhotoToFile(imageRequest.expertId(), imageRequest.filePath());
        return ResponseEntity.ok("Photo has been saved to " + imageRequest.filePath());
    }
}
