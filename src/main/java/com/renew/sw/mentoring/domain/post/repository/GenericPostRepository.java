package com.renew.sw.mentoring.domain.post.repository;

import com.renew.sw.mentoring.domain.post.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GenericPostRepository<T extends Post> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    @Override
    @EntityGraph(attributePaths = {"user"})
    Page<T> findAll(Specification<T> spec, Pageable pageable);
}
