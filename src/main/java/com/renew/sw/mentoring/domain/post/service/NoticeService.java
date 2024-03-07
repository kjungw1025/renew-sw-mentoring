package com.renew.sw.mentoring.domain.post.service;

import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestCreateNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.response.ResponseSingleNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.NoticeRepository;
import com.renew.sw.mentoring.domain.post.repository.spec.PostSpec;
import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.global.error.exception.NotGrantedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService{

    private final GenericPostService<Notice> noticeService;
    private final NoticeRepository repository;

    @Transactional
    public Long create(Long userId, RequestCreateNoticeDto dto) {
        return noticeService.create(repository, userId, dto);
    }

    public Page<SummarizedGenericPostDto> listNotice(String keyword, Pageable pageable, int bodySize) {
        Specification<Notice> spec = PostSpec.withTitleOrBody(keyword);
        return noticeService.list(repository, spec, pageable, bodySize);
    }

    public ResponseSingleNoticeDto findOne(Long id, Long userId, UserRole role) {
        return noticeService.findOne(repository, id, userId, role, ResponseSingleNoticeDto::new);
    }

    @Transactional
    public void update(Long userId, Long id, RequestUpdateNoticeDto dto) {
        noticeService.update(repository, userId, id, dto);
    }

    @Transactional
    public void delete(Long userId, Long id, UserRole role) {
        Notice notice = repository.findById(id).orElseThrow(PostNotFoundException::new);
        if ((!notice.getUser().getId().equals(userId)) || (role != UserRole.ADMIN)){
            throw new NotGrantedException();
        }
        repository.delete(notice);
    }
}
