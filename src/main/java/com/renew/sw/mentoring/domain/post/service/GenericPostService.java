package com.renew.sw.mentoring.domain.post.service;

import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.Post;
import com.renew.sw.mentoring.domain.post.model.entity.PostFile;
import com.renew.sw.mentoring.domain.post.model.entity.PostImage;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestCreateGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.response.ResponseSingleGenericPostDto;
import com.renew.sw.mentoring.domain.post.repository.GenericPostRepository;
import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.global.error.exception.NotGrantedException;
import com.renew.sw.mentoring.infra.s3.model.dto.FileRequest;
import com.renew.sw.mentoring.infra.s3.model.dto.ImageRequest;
import com.renew.sw.mentoring.infra.s3.model.dto.UploadedFile;
import com.renew.sw.mentoring.infra.s3.model.dto.UploadedImage;
import com.renew.sw.mentoring.infra.s3.service.AWSObjectStorageService;
import com.renew.sw.mentoring.infra.s3.service.FileUploadService;
import com.renew.sw.mentoring.infra.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericPostService<E extends Post> {

    protected final UserRepository userRepository;

    protected final AWSObjectStorageService s3service;
    protected final ImageUploadService imageUploadService;
    protected final FileUploadService fileUploadService;

    /**
     * 게시글 전체 조회
     *
     * @param spec       검색어
     * @param bodySize   본문 길이
     */
    @Transactional(readOnly = true)
    public Page<SummarizedGenericPostDto> list(GenericPostRepository<E> repository, Specification<E> spec,
                                               Pageable pageable, int bodySize) {
        Page<E> result = list(repository, spec, pageable);
        return result.map((post) -> makeListDto(bodySize, post));
    }

    @Transactional(readOnly = true)
    public <T> Page<T> list(GenericPostRepository<E> repository, Specification<E> spec, Pageable pageable, int bodySize,
                            PostResultMapper<T, SummarizedGenericPostDto, E> mapper) {
        Page<E> result = list(repository, spec, pageable);
        return result.map((post) -> {
            SummarizedGenericPostDto dto = makeListDto(bodySize, post);
            return mapper.map(dto, post);
        });
    }

    private Page<E> list(GenericPostRepository<E> repository, Specification<E> spec, Pageable pageable) {
        if (spec == null) {
            spec = Specification.where(null);
        }
        return repository.findAll(spec, pageable);
    }

    public SummarizedGenericPostDto makeListDto(int bodySize, E post) {
        return new SummarizedGenericPostDto(s3service, bodySize, post);
    }

    /**
     * 게시글 등록
     */
    @Transactional
    public Long create(GenericPostRepository<E> repository, Long userId, RequestCreateGenericPostDto<E> dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        E post = dto.toEntity(user);

        attachImages(dto.getImages(), post);
        attachFiles(dto.getFiles(), post);

        E savedPost = repository.save(post);
        return savedPost.getId();
    }

    private void attachImages(List<MultipartFile> dtoImages, E post) {
        List<UploadedImage> images = imageUploadService.uploadedImages(
                ImageRequest.ofList(dtoImages)
        );

        List<PostImage> postImages = new ArrayList<>();
        for (UploadedImage image : images) {
            PostImage.PostImageBuilder builder = PostImage.builder()
                    .imageName(image.getOriginalImageName())
                    .contentType(image.getMimeType().toString())
                    .imageId(image.getFileId());

            postImages.add(builder.build());
        }
        for (PostImage file : postImages) {
            file.changePost(post);
        }
    }

    private void attachFiles(List<MultipartFile> dtoFiles, E post) {
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

    /**
     * 게시글 단건 조회
     */
    @Transactional
    public ResponseSingleGenericPostDto findOne(GenericPostRepository<E> repository, Long postId, @Nullable Long userId,
                                                UserRole role) {
        E post = findPost(repository, postId, role);
        return makePostDto(userId, post);
    }

    @Transactional
    public <T> T findOne(GenericPostRepository<E> repository, Long postId, Long userId, UserRole role,
                         PostResultMapper<T, ResponseSingleGenericPostDto, E> mapper) {
        E post = findPost(repository, postId, role);
        ResponseSingleGenericPostDto dto = makePostDto(userId, post);

        try {
            return mapper.map(dto, post);
        } catch (ClassCastException e) {
            throw new PostNotFoundException();
        }
    }

    private ResponseSingleGenericPostDto makePostDto(@Nullable Long userId, E post) {
        boolean isMine = false;

        if (userId != null) {
            isMine = post.getUser().getId().equals(userId);
        }
        return new ResponseSingleGenericPostDto(s3service, isMine, post);
    }

    private E findPost(GenericPostRepository<E> repository, Long postId, UserRole role) {
        Optional<E> post;
        if (role.isAdmin()) {
            // TODO : 관리자 권한일 때 조회할 수 있는 범위 확장 가능
            post = repository.findById(postId);
        } else {
            post = repository.findById(postId);
        }
        return post.orElseThrow(PostNotFoundException::new);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void update(GenericPostRepository<E> repository, Long postId, Long userId, RequestUpdateGenericPostDto<E> dto) {
        E post = findPost(repository, postId, UserRole.USER);
        if (!post.getUser().getId().equals(userId)) {
            throw new NotGrantedException();
        }
        post.update(dto.getTitle(), dto.getBody());

        repository.save(post);
    }

    // TODO : 미션 승인과 관련된 로직을 작성

    @FunctionalInterface
    public interface PostResultMapper<T, D, E extends Post> {
        T map(D dto, E post);
    }
}
