package com.community.backend.ServiceTest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
public class MockFileGenerator {
    public MockMultipartFile MockFile() {
        byte[] randomBytes = new byte[16];
        new java.security.SecureRandom().nextBytes(randomBytes);

        return new MockMultipartFile(
                "file",                   // form field name
                "random.bin",                   // filename
                "application/octet-stream",     // content type
                randomBytes                     // actual binary content
        );
    }
}
