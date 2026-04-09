package com.geunnseung.daengnyangrefactor.comment.repository;

import com.geunnseung.daengnyangrefactor.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            select comment
            from Comment comment
            join fetch comment.author
            where comment.petPost.id = :petPostId
              and comment.deletedAt is null
            order by comment.createdAt asc
            """)
    List<Comment> findAllWithAuthorByPetPostId(@Param("petPostId") Long petPostId);
}
