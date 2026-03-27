package com.geunnseung.daengnyangrefactor.global.storage;

public record MediaUploadResult(

        String fileUrl,
        String objectKey,
        String contentType,
        Long fileSize
) {
}
