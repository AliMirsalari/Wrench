package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.service.AdminService;
import com.ali.mirsalari.wrench.controller.dto.response.AdminResponse;
import com.ali.mirsalari.wrench.controller.dto.request.ChangePasswordRequest;
import com.ali.mirsalari.wrench.controller.dto.request.RegisterAdminRequest;
import com.ali.mirsalari.wrench.controller.mapper.AdminResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/admin")
public class AdminController {
    private final AdminResponseMapper adminResponseMapper;
    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"","/"})
    public List<AdminResponse> getAdmins() {
        List<Admin> admins = adminService.findAll();
        return admins.stream()
                .map(adminResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
            Admin admin = adminService.save(
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.password());
            return ResponseEntity.ok(adminResponseMapper.toDto(admin));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/deleteAdmin")
    public ResponseEntity<?> deleteAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        adminService.remove(userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/updateExpert")
    public ResponseEntity<?> updateAdmin(
                                         @Valid @RequestBody RegisterAdminRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails) {
            Admin admin = adminService.update(
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.password(),
                    userDetails
            );
            return ResponseEntity.ok(adminResponseMapper.toDto(admin));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<AdminResponse> findById(@PathVariable Long id) {
        Optional<Admin> admin = Optional.ofNullable(adminService.findById(id));

        return admin.map(adminResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<AdminResponse> findByEmail(@PathVariable String email) {
        Optional<Admin> admin = Optional.ofNullable(adminService.findByEmail(email));

        return admin.map(adminResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody ChangePasswordRequest request) {
            if (!Objects.equals(request.newPassword(), request.confirmNewPassword())){
                throw new  IllegalArgumentException("Passwords must be the same!");
            }
            Admin admin = adminService.changePassword(request.newPassword(), request.oldPassword(), userDetails.getUsername());
            return ResponseEntity.ok(adminResponseMapper.toDto(admin));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getRegisterDate/{id}")
    public ResponseEntity<String> getRegisterDate(@PathVariable Long id) {
        Optional<Admin> adminOptional = Optional.ofNullable(adminService.findById(id));

        return adminOptional.map(admin -> {
            Instant registerTime = admin.getRegisterTime();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(registerTime, zoneId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zoneId);
            String formattedTime = formatter.format(zonedDateTime);

            return ResponseEntity.ok(formattedTime);
        }).orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getCreditAmount")
    public ResponseEntity<?> getCreditAmount(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<Admin> adminOptional = Optional.ofNullable(adminService.findByEmail(userDetails.getUsername()));

        return adminOptional.map(admin -> ResponseEntity.ok(admin.getCredit()))
                .orElse(ResponseEntity.notFound().build());
    }
}
