package com.geunnseung.daengnyangrefactor.pet.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PetSpecies species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PetGender gender;

    private LocalDate birthDate;

    @Column(length = 500)
    private String profileImageUrl;

    public static Pet register(
            final User owner,
            final String name,
            final PetSpecies species,
            final PetGender gender,
            final LocalDate birthDate,
            final String profileImageUrl
    ) {
        return new Pet(owner, name, species, gender, birthDate, profileImageUrl);
    }

    private Pet(
            final User owner,
            final String name,
            final PetSpecies species,
            final PetGender gender,
            final LocalDate birthDate,
            final String profileImageUrl
    ) {
        this.owner = owner;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImageUrl = profileImageUrl;
    }

    public void assignGroup(final Group group) {
        this.group = group;
    }
}
