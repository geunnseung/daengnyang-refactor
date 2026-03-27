package com.geunnseung.daengnyangrefactor.global.storage;

public interface MediaStoragePort {

    MediaUploadResult store(MediaUploadCommand command);
}
