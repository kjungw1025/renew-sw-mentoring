package com.renew.sw.mentoring.domain.comment.repository;

import com.renew.sw.mentoring.domain.comment.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c " +
            "join fetch c.post " +
            "where c.post.id = :postId and c.parentCommentId is null " +
            "order by c.createdAt asc ")
    List<Comment> findByPostIdExceptReply(@Param("postId") Long postId);

    @Query("select c from Comment c " +
            "join fetch c.post " +
            "where c.parentCommentId=:id order by c.createdAt asc ")
    List<Comment> findAllReplies(Long id);
}
