package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

public record PetPostFileUploadUrlResponse(

        String uploadUrl,
        String objectKey,
        String fileUrl,
        String contentType,
        Long fileSize
) {
}
