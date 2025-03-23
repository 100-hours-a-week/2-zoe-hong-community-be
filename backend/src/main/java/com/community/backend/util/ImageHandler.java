package com.community.backend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageHandler {

    private static void validate(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지가 존재하지 않거나 비어 있습니다.");
        }
    }

    public static String saveImage(MultipartFile image, Boolean isEssential) {
        if (isEssential) {
            validate(image);
        }
        try {
            String originalName = image.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new IllegalArgumentException("파일에 확장자가 없습니다.");
            }

            String ext = originalName.substring(originalName.lastIndexOf("."));
            String filename = UUID.randomUUID() + ext;

            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
            Path path = Paths.get(uploadDir + filename);

            Files.createDirectories(path.getParent());
            System.out.println("저장 경로: " + path.toAbsolutePath());

            image.transferTo(path.toFile());

            return "/images/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류 발생", e);
        }
    }
}
