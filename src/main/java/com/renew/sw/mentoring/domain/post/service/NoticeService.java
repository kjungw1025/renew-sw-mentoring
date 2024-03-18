package com.renew.sw.mentoring.domain.post.service;

import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.PostFile;
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
import com.renew.sw.mentoring.infra.s3.model.dto.FileRequest;
import com.renew.sw.mentoring.infra.s3.model.dto.UploadedFile;
import com.renew.sw.mentoring.infra.s3.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService{

    private final GenericPostService<Notice> noticeService;
    private final FileUploadService fileUploadService;
    private final NoticeRepository repository;

    @Transactional
    public Long create(Long userId, RequestCreateNoticeDto dto) {
        Long postId = noticeService.create(repository, userId, dto);
        Notice notice = repository.findById(postId).orElseThrow(PostNotFoundException::new);

        attachFiles(dto.getFiles(), notice);

        return postId;
    }

    private void attachFiles(List<MultipartFile> dtoFiles, Notice post) {
        List<UploadedFile> files = fileUploadService.uploadedFiles(
                FileRequest.ofList(dtoFiles)
        );

        List<PostFile> postFiles = new ArrayList<>();
        for (UploadedFile file: files) {
            PostFile.PostFileBuilder builder = PostFile.builder()
                    .fileName(file.getOriginalFileName())
                    .contentType(file.getMimeType().toString())
                    .fileId(file.getFileId());

            postFiles.add(builder.build());
        }
        for (PostFile file : postFiles) {
            file.changePost(post);
        }
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
