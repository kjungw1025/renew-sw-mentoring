package com.renew.sw.mentoring.domain.comment;

import com.renew.sw.mentoring.domain.comment.exception.CommentNotFoundException;
import com.renew.sw.mentoring.domain.comment.model.CommentStatus;
import com.renew.sw.mentoring.domain.comment.model.dto.response.SummarizedCommentDto;
import com.renew.sw.mentoring.domain.comment.model.dto.response.SummarizedReplyDto;
import com.renew.sw.mentoring.domain.comment.model.entity.Comment;
import com.renew.sw.mentoring.domain.comment.repository.CommentRepository;
import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.post.repository.PostRepository;
import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.global.error.exception.NotGrantedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<SummarizedCommentDto> list(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdExceptReply(postId);
        return comments.stream().map(e -> {
            List<Comment> replies = commentRepository.findAllReplies(e.getId());
            return new SummarizedCommentDto(e, replies.stream().map(SummarizedReplyDto::new).toList());
        }).collect(Collectors.toList());
    }

    @Transactional
    public Long create(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .commentStatus(CommentStatus.ACTIVE)
                .build();

        comment.changePost(post);
        comment = commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void edit(Long commentId, Long userId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if(!comment.getUser().getId().equals(userId)) {
            throw new NotGrantedException();
        }
        if(comment.getCommentStatus() == CommentStatus.ACTIVE
                || comment.getCommentStatus() == CommentStatus.EDITED) {
            comment.updateContent(content);
        }
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if(comment.getUser().getUserRole().isAdmin()) {
            comment.markedAsDeleted(true);
        } else if(comment.getUser().getId().equals(userId)) {
            comment.markedAsDeleted(false);
        } else {
            throw new NotGrantedException();
        }
    }

    @Transactional
    public Long createReply(Long commentId, Long userId, String content) {
        Comment parentComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Long postId = parentComment.getPost().getId();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .commentStatus(CommentStatus.ACTIVE)
                .parentCommentId(parentComment.getId())
                .build();

        comment.changePost(post);
        comment = commentRepository.save(comment);
        parentComment.addChildComment(comment);
        return comment.getId();
    }
}
