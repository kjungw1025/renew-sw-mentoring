package com.renew.sw.mentoring.domain.post.repository;

import com.renew.sw.mentoring.domain.post.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
