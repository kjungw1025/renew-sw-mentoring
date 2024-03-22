package com.renew.sw.mentoring.domain.post.controller;

import com.renew.sw.mentoring.domain.comment.CommentService;
import com.renew.sw.mentoring.domain.comment.model.dto.request.RequestCreateCommentDto;
import com.renew.sw.mentoring.domain.comment.model.dto.response.SummarizedCommentDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestCreateNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.response.ResponseSingleNoticeDto;
import com.renew.sw.mentoring.domain.post.service.NoticeService;
import com.renew.sw.mentoring.domain.team.model.dto.response.ResponsePage;
import com.renew.sw.mentoring.global.auth.jwt.AppAuthentication;
import com.renew.sw.mentoring.global.auth.role.AdminAuth;
import com.renew.sw.mentoring.global.auth.role.UserAuth;
import com.renew.sw.mentoring.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "공지사항", description = "공지사항 관련 API")
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final CommentService commentService;

    /**
     * 공지사항 목록 조회
     *
     * @param keyword    검색어
     * @param pageable   페이지 정보
     * @param bodySize   본문 길이(설정하지 않으면 기본 50)
     */
    @GetMapping
    public ResponsePage<SummarizedGenericPostDto> list(@RequestParam(required = false) String keyword,
                                                       @ParameterObject Pageable pageable,
                                                       @RequestParam(defaultValue = "50") int bodySize) {
        Page<SummarizedGenericPostDto> result = noticeService.listNotice(keyword, pageable, bodySize);
        return new ResponsePage<>(result);
    }

    /**
     * 공지사항 글 작성
     *
     * @param auth   사용자 정보
     * @return       생성된 글 id
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminAuth
    public ResponseIdDto create(AppAuthentication auth, @ModelAttribute RequestCreateNoticeDto dto) {
        Long result = noticeService.create(auth.getUserId(), dto);
        return new ResponseIdDto(result);
    }

    /**
     * 공지사항 글 상세 조회
     *
     * @param id     글 id
     * @param auth   사용자 정보
     */
    @GetMapping("/{id}")
    @UserAuth
    public ResponseSingleNoticeDto findOne(@PathVariable Long id, AppAuthentication auth) {
        return noticeService.findOne(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 공지사항 글 수정
     *
     * @param id     글 id
     * @param auth   사용자 정보
     */
    @PatchMapping("/{id}")
    @AdminAuth
    public void update(@PathVariable Long id, AppAuthentication auth, @RequestBody RequestUpdateNoticeDto dto) {
        noticeService.update(auth.getUserId(), id, dto);
    }

    /**
     * 공지사항 글 삭제
     *
     * @param id     글 id
     * @param auth   사용자 정보
     */
    @DeleteMapping("/{id}")
    @AdminAuth
    public void delete(@PathVariable Long id, AppAuthentication auth) {
        noticeService.delete(auth.getUserId(), id, auth.getUserRole());
    }

    /**
     * 모든 댓글을 조회합니다.
     */
    @GetMapping("/comment/{postId}")
    public List<SummarizedCommentDto> listComments(@PathVariable Long postId) {
        return commentService.list(postId);
    }

    /**
     * 게시글에 댓글을 생성합니다.
     **/
    @PostMapping("/comment/{postId}")
    @UserAuth
    public ResponseIdDto createComment(AppAuthentication auth,
                                @PathVariable Long postId,
                                @Valid @RequestBody RequestCreateCommentDto dto) {
        Long result = commentService.create(postId, auth.getUserId(), dto.getContent());
        return new ResponseIdDto(result);
    }

    /**
     * 댓글을 수정합니다.
     * <p>대댓글도 수정할 수 있습니다.</p>
     */
    @PatchMapping("/comment/{commentId}")
    @UserAuth
    public void editComment(AppAuthentication auth,
                     @PathVariable Long commentId,
                     @Valid @RequestBody RequestCreateCommentDto dto) {
        commentService.edit(commentId, auth.getUserId(), dto.getContent());
    }

    /**
     * 댓글을 삭제합니다.
     * <p>대댓글도 삭제할 수 있습니다.</p>
     */
    @DeleteMapping("/comment/{commentId}")
    @UserAuth
    public void deleteComment(AppAuthentication auth,
                       @PathVariable Long commentId) {
        commentService.delete(commentId, auth.getUserId());
    }

    /**
     * 대댓글을 생성합니다.
     *
     * @param commentId   댓글 ID
     */
    @PostMapping("/reply/{commentId}")
    @UserAuth
    public ResponseIdDto createReply(AppAuthentication auth,
                                     @PathVariable Long commentId,
                                     @Valid @RequestBody RequestCreateCommentDto dto) {
        Long result = commentService.createReply(commentId, auth.getUserId(), dto.getContent());
        return new ResponseIdDto(result);
    }
}
