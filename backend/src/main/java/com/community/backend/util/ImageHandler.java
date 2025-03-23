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

    public static String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지가 존재하지 않거나 비어 있습니다.");
        }

        try {
            String uploadDir = "uploads/images/";
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);

            Files.createDirectories(path.getParent());
            image.transferTo(path.toFile());

            return "/images/" + filename;
        } catch (IOException e) {
            // 예외를 런타임 예외로 감싸서 던짐 → 호출부에서는 try-catch 안 해도 됨
            throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.", e);
        }
    }
}
