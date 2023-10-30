package com.ali.mirsalari.wrench.repository;

public interface ImageRepository {
    void writePhotoToFile(String filePath, byte[] photoData);
}
