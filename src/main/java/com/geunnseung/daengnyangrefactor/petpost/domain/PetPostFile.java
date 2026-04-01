package com.geunnseung.daengnyangrefactor.petpost.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "pet_post_files",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pet_post_file_pet_post_id", columnNames = "pet_post_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetPostFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_post_id", nullable = false)
    private PetPost petPost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PetPostFileType fileType;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    @Column(nullable = false, length = 500)
    private String objectKey;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    public static PetPostFile create(
            final PetPost petPost,
            final PetPostFileType fileType,
            final String fileUrl,
            final String objectKey,
            final String contentType,
            final Long fileSize
    ) {
        return new PetPostFile(petPost, fileType, fileUrl, objectKey, contentType, fileSize);
    }

    private PetPostFile(
            final PetPost petPost,
            final PetPostFileType fileType,
            final String fileUrl,
            final String objectKey,
            final String contentType,
            final Long fileSize
    ) {
        this.petPost = petPost;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.objectKey = objectKey;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
}
