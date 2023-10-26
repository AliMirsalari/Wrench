package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.dto.*;
import com.ali.mirsalari.wrench.service.mapper.ExpertResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
    public ResponseEntity<?> registerExpert(@Valid RegisterExpertRequest request, @RequestPart MultipartFile image) {
            File file = convertMultipartFileToFile(image);
            System.out.println(file.getAbsolutePath());
            Expert expert = expertService.save(request.firstName(), request.lastName(), request.email(), request.password(), file.getAbsolutePath());
            return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateExpert(@Valid @PathVariable("id") Long id,
                                          @RequestPart MultipartFile image,
                                          @Valid RegisterExpertRequest request) {
            File file = convertMultipartFileToFile(image);
            System.out.println(file.getAbsolutePath());
            Expert expert = expertService.update(id, request.firstName(), request.lastName(), request.email(), request.password(), file.getAbsolutePath());
            return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @DeleteMapping(path = "{expertId}")
    public HttpStatus deleteExpert(@PathVariable("expertId") Long expertId) {
        expertService.remove(expertId);
        return HttpStatus.OK;
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
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
            Expert expert = expertService.changePassword(request.newPassword(), request.oldPassword(), userId);
            return ResponseEntity.ok(expertResponseMapper.toDto(expert));
    }

    @PostMapping("/addSkill")
    public ResponseEntity<?> addSkillToExpert(@RequestBody SkillRequest request) {
            expertService.addSkill(request.skillId(), request.expertId());
            return ResponseEntity.ok("Skill is added!");
    }

    @PostMapping("/removeSkill")
    public ResponseEntity<?> removeSkillToExpert(@RequestBody SkillRequest request) {
            expertService.removeSkill(request.skillId(), request.expertId());
            return ResponseEntity.ok("Skill is removed!");
    }

    @PostMapping("/savePhotoToFile")
    public ResponseEntity<String> savePhotoToFile(@RequestBody ImageRequest imageRequest) {
            expertService.retrieveAndSavePhotoToFile(imageRequest.expertId(), imageRequest.filePath());
            return ResponseEntity.ok("Photo has been saved to " + imageRequest.filePath());
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to File", e);
        }
        return file;
    }
}
