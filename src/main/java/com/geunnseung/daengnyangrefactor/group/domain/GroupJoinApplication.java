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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "group_join_applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_join_applications_group_id_requester_id",
                        columnNames = {"group_id", "requester_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GroupJoinApplication extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupJoinApplicationStatus status;

    private GroupJoinApplication(
            final Group group,
            final User requester) {
        this.group = group;
        this.requester = requester;
        this.status = GroupJoinApplicationStatus.PENDING;
    }

    public static GroupJoinApplication create(
            final Group group,
            final User requester
    ) {
        return new GroupJoinApplication(group, requester);
    }

    public void approve() {
        this.status = GroupJoinApplicationStatus.APPROVED;
    }

    public void reject() {
        this.status = GroupJoinApplicationStatus.REJECTED;
    }
}
