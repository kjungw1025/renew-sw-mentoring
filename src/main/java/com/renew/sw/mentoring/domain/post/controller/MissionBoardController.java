package com.renew.sw.mentoring.domain.post.controller;

import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestCreateMissionBoardDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateMissionBoardDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.response.ResponseMissionBoardDto;
import com.renew.sw.mentoring.domain.post.repository.MissionBoardRepository;
import com.renew.sw.mentoring.domain.post.service.GenericPostService;
import com.renew.sw.mentoring.domain.post.service.MissionBoardService;
import com.renew.sw.mentoring.global.auth.jwt.AppAuthentication;
import com.renew.sw.mentoring.global.auth.role.MentorAuth;
import com.renew.sw.mentoring.global.auth.role.UserAuth;
import com.renew.sw.mentoring.global.model.dto.ResponseIdDto;
import com.renew.sw.mentoring.global.model.dto.ResponsePage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "미션 인증 게시판", description = "미션 인증 관련 api")
@RestController
@RequestMapping("/post/mission-board")
@RequiredArgsConstructor
public class MissionBoardController {

    private final MissionBoardService missionBoardService;

    /**
     * 미션 인증 글 등록
     * <p>
     *     미션 인증 글은 멘토들만 작성이 가능합니다.
     * </p>
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MentorAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @ModelAttribute RequestCreateMissionBoardDto request) {
        Long postId = missionBoardService.create(auth.getUserId(), request);
        return new ResponseIdDto(postId);
    }

    /**
     * 게시글 목록 조회
     * @param keyword   제목이나 내용에 포함된 검색어. 지정하지 않으면 모든 게시글 조회.
     * @param bodySize  지정하지 않으면 10
     * @param pageable  페이징 size, sort, page
     * @return          페이징된 미션 인증 게시글 목록
     */
    @GetMapping
    public ResponsePage<SummarizedGenericPostDto> list(@RequestParam(required = false) String keyword,
                                                       @RequestParam(defaultValue = "10") int bodySize,
                                                       @ParameterObject Pageable pageable) {
        Page<SummarizedGenericPostDto> list = missionBoardService.list(keyword, pageable, bodySize);
        return new ResponsePage<>(list);
    }

    /**
     * 게시글 단건 조회
     *
     * @param id    조회할 게시글 id
     * @return      미션 인증 게시글 정보
     */
    @GetMapping("/{id}")
    @UserAuth
    public ResponseMissionBoardDto findOne(AppAuthentication auth,
                                           @PathVariable Long id) {
        return missionBoardService.findOne(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 내가 쓴 글 조회
     * @return      내가 작성한 미션 인증 게시글 정보들
     *
     * <p>
     *     멘토들에 해당됩니다.
     * </p>
     */
    @GetMapping("/my")
    @MentorAuth
    public ResponsePage<SummarizedGenericPostDto> listMyPosts(AppAuthentication auth,
                                                              @ParameterObject Pageable pageable,
                                                              @RequestParam(defaultValue = "10") int bodySize) {
        Page<SummarizedGenericPostDto> posts = missionBoardService.listMyPosts(auth.getUserId(), pageable, bodySize);
        return new ResponsePage<>(posts);
    }

    /**
     * 미션 인증 글 수정
     *
     * @param auth  사용자 정보
     * @param id    글 id
     */
    @PatchMapping("/{id}")
    @MentorAuth
    public void update(AppAuthentication auth,
                       @PathVariable Long id,
                       @RequestBody RequestUpdateMissionBoardDto dto) {
        missionBoardService.update(auth.getUserId(), id, dto);
    }

    /**
     * 미션 인증 글 삭제
     *
     * @param auth  사용자 정보
     * @param id    글 id
     */
    @DeleteMapping("/{id}")
    @MentorAuth
    public void delete(AppAuthentication auth,
                       @PathVariable Long id) {
        missionBoardService.delete(auth.getUserId(), id, auth.getUserRole());
    }
}
