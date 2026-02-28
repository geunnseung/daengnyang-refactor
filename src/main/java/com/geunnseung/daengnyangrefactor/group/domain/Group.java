package com.geunnseung.daengnyangrefactor.group.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 255)
    private String description;

    public static Group create(
            final String name,
            final String description
    ) {
        Group group = new Group();
        group.name = name;
        group.description = description;
        return group;
    }
}
