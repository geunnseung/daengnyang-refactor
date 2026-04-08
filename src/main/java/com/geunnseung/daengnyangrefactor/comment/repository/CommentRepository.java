package com.geunnseung.daengnyangrefactor.comment.repository;

import com.geunnseung.daengnyangrefactor.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
