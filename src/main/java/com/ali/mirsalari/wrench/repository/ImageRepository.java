package com.ali.mirsalari.wrench.repository;

import java.io.IOException;

public interface ImageRepository {
    void writePhotoToFile(String filePath, byte[] photoData);
}
