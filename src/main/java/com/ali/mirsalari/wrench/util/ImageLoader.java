package com.ali.mirsalari.wrench.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageLoader {
    public static byte[] loadImageBytes(String imagePath) throws IOException, IOException {
        Path path = Path.of(imagePath);
        return Files.readAllBytes(path);
    }
}