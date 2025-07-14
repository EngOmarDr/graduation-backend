package com.graduationProject._thYear.Product.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
@Service
@Slf4j
public class ImageStorageService {



    private String saveImageToDisk(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/images";
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Could not create directory: " + uploadDir);
            }

            String originalName = image.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new RuntimeException("Invalid image file name");
            }

            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String uniqueFileName = UUID.randomUUID() + extension;
            String fullPath = uploadDir + File.separator + uniqueFileName;

            System.out.println("Saving to: " + fullPath);

            image.transferTo(new File(fullPath));

            return "/images/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace(); // Log the real error
            throw new RuntimeException("Failed to store image", e);
        }
    }


    private final String TEMP_DIR = System.getProperty("user.dir") + "/uploads/tmp";
    private final String IMAGE_DIR = System.getProperty("user.dir") + "/uploads/images";

    public String saveToTemp(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;

        try {
            File dir = new File(TEMP_DIR);
            if (!dir.exists()) dir.mkdirs();

            String originalName = image.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new RuntimeException("Invalid image file name");
            }

            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String uniqueFileName = UUID.randomUUID() + extension;
            String fullPath = TEMP_DIR + File.separator + uniqueFileName;

            image.transferTo(new File(fullPath));
            log.info("Image saved to TEMP: {}", fullPath);
            return fullPath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image temporarily", e);
        }
    }

    public String moveToPermanent(String tempPath) {
        try {
            File tempFile = new File(tempPath);
            if (!tempFile.exists()) return null;

            File imageDir = new File(IMAGE_DIR);
            if (!imageDir.exists()) imageDir.mkdirs();

            String fileName = tempFile.getName();
            File newFile = new File(IMAGE_DIR + File.separator + fileName);
            Files.move(tempFile.toPath(), newFile.toPath());

            log.info("Image moved to permanent: {}", newFile.getAbsolutePath());
            return "/images/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to move image to permanent folder", e);
        }
    }

    public void deleteTemp(String tempPath) {
        if (tempPath == null || tempPath.isBlank()) return;

        File file = new File(tempPath);
        if (file.exists()) {
            file.delete();
            log.info("Temp image deleted: {}", file.getAbsolutePath());
        }
    }

    public void deletePermanent(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) return;

        String fullPath = IMAGE_DIR + File.separator + new File(imagePath).getName();
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
            log.info("Permanent image deleted: {}", fullPath);
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanOldTempImages() {
        File tempDir = new File(TEMP_DIR);
        File[] files = tempDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.lastModified() < System.currentTimeMillis() - 3600_000) { // delete every 1 hour
                file.delete();
                log.info("Auto-deleted old temp image: {}", file.getName());
            }
        }
    }


    private void deleteOldImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) return;

        String fullPath = System.getProperty("user.dir") + "/uploads" + imagePath;
        File file = new File(fullPath);
        if (file.exists()) file.delete();
    }

}
