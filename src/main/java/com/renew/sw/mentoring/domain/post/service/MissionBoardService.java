package com.renew.sw.mentoring.domain.post.service;

import com.renew.sw.mentoring.domain.mission.exception.MissionNotFoundException;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedMissionBoardDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestCreateMissionBoardDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateMissionBoardDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.response.ResponseMissionBoardDto;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.MissionBoardRepository;
import com.renew.sw.mentoring.domain.post.repository.spec.PostSpec;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.global.error.exception.NotGrantedException;
import com.renew.sw.mentoring.infra.s3.service.AWSObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionBoardService {
    private final GenericPostService<MissionBoard> postService;
    private final AWSObjectStorageService s3service;

    private final MissionBoardRepository repository;

    public Long create(Long userId, RequestCreateMissionBoardDto dto) {
        return postService.create(repository, userId, dto);
    }

    public ResponseMissionBoardDto findOne(Long id, Long userId, UserRole role) {
        return postService.findOne(repository, id, userId, role, ResponseMissionBoardDto::new);
    }

    public Page<SummarizedMissionBoardDto> list(String keyword, Pageable pageable, int bodySize) {
        Specification<MissionBoard> spec = PostSpec.withTitleOrBody(keyword);
        return postService.list(repository, spec, pageable, bodySize, (dto, post) ->
                new SummarizedMissionBoardDto(s3service, bodySize, post));
    }

    @Transactional(readOnly = true)
    public Page<SummarizedMissionBoardDto> listMyPosts(Long userId, Pageable pageable, int bodySize) {
        return repository.findAllByUserId(userId, pageable)
                .map(post -> new SummarizedMissionBoardDto(s3service, bodySize, post));
    }

    @Transactional
    public void update(Long userId, Long id, RequestUpdateMissionBoardDto dto) {
        postService.update(repository, userId, id, dto);
    }

    @Transactional
    public void delete(Long userId, Long id, UserRole role) {
        MissionBoard missionBoard = repository.findById(id).orElseThrow(PostNotFoundException::new);
        if ((!missionBoard.getUser().getId().equals(userId)) || (role != UserRole.MENTOR)){
            throw new NotGrantedException();
        }
        repository.delete(missionBoard);
    }
}
