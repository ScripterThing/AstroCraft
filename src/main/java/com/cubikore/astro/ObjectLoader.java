package com.cubikore.astro;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ObjectLoader {
    public static String getFileContents(String resourcePath) {
        try (InputStream in = AstroCraft.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("File not found: " + resourcePath);
            }
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
                return scanner.useDelimiter("\\A").next();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + resourcePath, e);
        }
    }
}
