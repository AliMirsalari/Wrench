package com.ali.mirsalari.wrench.repository.impl;

import com.ali.mirsalari.wrench.exception.WriteImageException;
import com.ali.mirsalari.wrench.repository.ImageRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    @Override
    public void writePhotoToFile(String filePath, byte[] photoData) {
        Path destinationPath = Path.of(filePath);
        try {
            Files.write(destinationPath, photoData);
        } catch (IOException e) {
            throw new WriteImageException(e);
        }
    }
}
