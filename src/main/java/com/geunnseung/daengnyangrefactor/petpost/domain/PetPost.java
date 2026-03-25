package com.geunnseung.daengnyangrefactor.petpost.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(length = 2000)
    private String content;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static PetPost create(
            final Pet pet,
            final User author,
            final String content
    ) {
        return new PetPost(pet, author, content);
    }

    private PetPost(
            final Pet pet,
            final User author,
            final String content
    ) {
        this.pet = pet;
        this.author = author;
        this.content = content;
    }
}
