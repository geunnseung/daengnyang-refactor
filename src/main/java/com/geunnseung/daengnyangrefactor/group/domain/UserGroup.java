package com.geunnseung.daengnyangrefactor.group.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserGroupRole role;

    public static UserGroup createAsOwner(
            final User user,
            final Group group
    ) {
        UserGroup userGroup = new UserGroup();
        userGroup.user = user;
        userGroup.group = group;
        userGroup.role = UserGroupRole.OWNER;
        return userGroup;
    }

    public static UserGroup createAsMember(
            final User user,
            final Group group
    ) {
        UserGroup userGroup = new UserGroup();
        userGroup.user = user;
        userGroup.group = group;
        userGroup.role = UserGroupRole.MEMBER;
        return userGroup;
    }
}
