package com.ali.mirsalari.wrench.annotation.impl;

import com.ali.mirsalari.wrench.annotation.ValidImage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class MultipartFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        if (!Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".jpg")) {
            return false;
        }

        return file.getSize() <= 300 * 1024;
    }
}
