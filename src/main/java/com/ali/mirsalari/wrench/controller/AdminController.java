package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.service.AdminService;
import com.ali.mirsalari.wrench.service.dto.AdminResponse;
import com.ali.mirsalari.wrench.service.dto.ChangePasswordRequest;
import com.ali.mirsalari.wrench.service.dto.RegisterAdminRequest;
import com.ali.mirsalari.wrench.service.mapper.AdminResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/admin")
public class AdminController {
    private final AdminResponseMapper adminResponseMapper;
    private final AdminService adminService;

    @GetMapping
    public List<AdminResponse> getAdmins() {
        List<Admin> admins = adminService.findAll();
        return admins.stream()
                .map(adminResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
            Admin admin = adminService.save(
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.password());
            return ResponseEntity.ok(adminResponseMapper.toDto(admin));
    }

    @DeleteMapping(path = "{adminId}")
    public HttpStatus deleteAdmin(@PathVariable("adminId") Long adminId) {
        adminService.remove(adminId);
        return HttpStatus.OK;
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateAdmin(@Valid
                                         @PathVariable("id") Long id,
                                         @RequestBody RegisterAdminRequest request) {
            Admin admin = adminService.update(
                    id,
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.password()
            );
            return ResponseEntity.ok(adminResponseMapper.toDto(admin));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<AdminResponse> findById(@PathVariable Long id) {
        Optional<Admin> admin = adminService.findById(id);

        return admin.map(adminResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<AdminResponse> findByEmail(@PathVariable String email) {
        Optional<Admin> admin = adminService.findByEmail(email);

        return admin.map(adminResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
            Admin admin = adminService.changePassword(request.newPassword(), request.oldPassword(), userId);
            return ResponseEntity.ok(adminResponseMapper.toDto(admin));
    }
}
