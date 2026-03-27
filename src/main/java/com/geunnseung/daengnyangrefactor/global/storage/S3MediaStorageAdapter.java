package com.geunnseung.daengnyangrefactor.global.storage;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3MediaStorageAdapter implements MediaStoragePort {

    private final S3Client s3Client;
    private final String bucket;
    private final String keyPrefix;
    private final String baseUrl;

    public S3MediaStorageAdapter(
            final S3Client s3Client,
            @Value("${app.s3.bucket}") final String bucket,
            @Value("${app.s3.key-prefix}") final String keyPrefix,
            @Value("${app.s3.base-url}") final String baseUrl
    ) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.keyPrefix = normalizePath(keyPrefix);
        this.baseUrl = removeTrailingSlash(baseUrl);
    }

    @Override
    public MediaUploadResult store(final MediaUploadCommand command) {
        MultipartFile file = command.file();
        if (file == null || file.isEmpty()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }

        String objectKey = createObjectKey(command.directory(), file.getOriginalFilename());
        String contentType = resolveContentType(file);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new DaengnyangException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return new MediaUploadResult(
                createFileUrl(objectKey),
                objectKey,
                contentType,
                file.getSize()
        );
    }

    private String createObjectKey(final String directory, final String originalFilename) {
        String normalizedDirectory = normalizePath(directory);
        String extension = extractExtension(originalFilename);
        String filename = UUID.randomUUID() + extension;

        if (keyPrefix.isBlank()) {
            return normalizedDirectory + "/" + filename;
        }

        return keyPrefix + "/" + normalizedDirectory + "/" + filename;
    }

    private String extractExtension(final String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }

        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    private String resolveContentType(final MultipartFile file) {
        if (file.getContentType() == null || file.getContentType().isBlank()) {
            return "application/octet-stream";
        }

        return file.getContentType();
    }

    private String createFileUrl(final String objectKey) {
        return baseUrl + "/" + objectKey;
    }

    private String normalizePath(final String path) {
        if (path == null || path.isBlank()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }

        return path.strip()
                .replaceAll("^/+", "")
                .replaceAll("/+$", "");
    }

    private String removeTrailingSlash(final String value) {
        if (value == null || value.isBlank()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }

        return value.strip().replaceAll("/+$", "");
    }
}

