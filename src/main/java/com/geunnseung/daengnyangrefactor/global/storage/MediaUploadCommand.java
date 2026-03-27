package com.geunnseung.daengnyangrefactor.global.storage;

import org.springframework.web.multipart.MultipartFile;

public record MediaUploadCommand(

        MultipartFile file,
        String directory
) {
}
