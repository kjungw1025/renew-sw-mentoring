package com.renew.sw.mentoring.domain.post.service;

import com.renew.sw.mentoring.domain.post.exception.PostNotFoundException;
import com.renew.sw.mentoring.domain.post.model.entity.dto.list.SummarizedGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestCreateNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.request.RequestUpdateNoticeDto;
import com.renew.sw.mentoring.domain.post.model.entity.dto.response.ResponseSingleGenericPostDto;
import com.renew.sw.mentoring.domain.post.model.entity.type.Notice;
import com.renew.sw.mentoring.domain.post.repository.GenericPostRepository;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.infra.s3.service.FileUploadService;
import com.renew.sw.mentoring.infra.s3.service.ImageUploadService;
import com.renew.sw.mentoring.mock.MultipartFileMock;
import com.renew.sw.mentoring.util.DummyPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericPostServiceTest {

    @Mock
    private GenericPostRepository<Notice> noticeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageUploadService imageUploadService;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private GenericPostService<Notice> noticeService;

    private final List<Notice> noticeList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        noticeRepository.deleteAll();
    }

    @Test
    @DisplayName("list가 잘 동작하는지?")
    public void list() {
        // given
        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .build();

        for (int i = 0; i < 3; i++) {
            Notice notice = Notice.builder()
                    .title("title" + i)
                    .body("body" + i)
                    .user(user)
                    .build();
            noticeList.add(notice);
        }

        Page<Notice> noticePage = new DummyPage<>(noticeList, 20);
        when(noticeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(noticePage);

        // when
        Page<SummarizedGenericPostDto> pages = noticeService.list(noticeRepository, null, Pageable.unpaged(), 100);

        // then
        assertThat(pages.getTotalElements()).isEqualTo(noticeList.size());
        for (int i = 0; i < pages.getTotalElements(); i++) {
            SummarizedGenericPostDto dto = pages.getContent().get(i);
            Notice notice = noticeList.get(i);
            assertThat(dto.getId()).isEqualTo(notice.getId());
            assertThat(dto.getTitle()).isEqualTo(notice.getTitle());
            assertThat(dto.getBody()).isEqualTo(notice.getBody());
            assertThat(dto.getAuthor()).isEqualTo(notice.getUser().getNickname());
        }
    }

    @Test
    @DisplayName("새롭게 잘 생성되는지?")
    public void create() {
        // given
        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .build();
        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .user(user)
                .build();

        List<MultipartFile> images = MultipartFileMock.createList(10);
        List<MultipartFile> files = MultipartFileMock.createList(10);
        RequestCreateNoticeDto dto = new RequestCreateNoticeDto("title", "body", images, files);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(noticeRepository.save(any())).thenReturn(notice);
        when(imageUploadService.uploadedImages(any())).thenReturn(new ArrayList<>());
        when(fileUploadService.uploadedFiles(any())).thenReturn(new ArrayList<>());

        // when
        Long result = noticeService.create(noticeRepository, user.getId(), dto);

        // then
        assertThat(result).isEqualTo(notice.getId());

        verify(imageUploadService).uploadedImages(argThat(list -> {
            for (int i = 0; i < images.size(); i++) {
                assertThat(list.get(i).getOriginalImageName()).isEqualTo(images.get(i).getOriginalFilename());
            }
            return true;
        }));

        verify(fileUploadService).uploadedFiles(argThat(list -> {
            for (int i = 0; i < files.size(); i++) {
                assertThat(list.get(i).getOriginalFileName()).isEqualTo(files.get(i).getOriginalFilename());
            }
            return true;
        }));

        verify(noticeRepository).save(argThat(entity -> {
            assertThat(entity.getUser()).isEqualTo(user);
            return true;
        }));
    }

    @Test
    @DisplayName("생성할 때 유저가 존재하지 않으면 오류")
    public void failedCreatedByUserNotFound() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThrows(UserNotFoundException.class, () ->
                noticeService.create(noticeRepository, 1L,
                        new RequestCreateNoticeDto("title", "body", List.of(), List.of())));
    }

    @Test
    @DisplayName("단건 조회가 잘 동작하는지?")
    public void findOne() {
        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .team(Team.builder().teamName("teamName").build())
                .build();
        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(notice, "user", user);

        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));

        // when
        ResponseSingleGenericPostDto dto = noticeService.findOne(noticeRepository, notice.getId(), notice.getUser().getId(), notice.getUser().getUserRole());

        // then
        verify(noticeRepository).findById(argThat(id -> {
            assertThat(id).isEqualTo(notice.getId());
            return true;
        }));

        assertThat(dto.getTitle()).isEqualTo("title");
        assertThat(dto.getBody()).isEqualTo("body");
        assertThat(dto.getAuthor()).isEqualTo(user.getNickname());
        assertThat(dto.isMine()).isEqualTo(true);
    }

    @Test
    @DisplayName("update가 잘 동작하는지?")
    public void update() {
        // given
        User user = User.builder()
                .name("name")
                .nickname("nickname")
                .password("password")
                .studentId("12345678")
                .userRole(UserRole.USER)
                .team(Team.builder().teamName("teamName").build())
                .build();
        Notice notice = Notice.builder()
                .title("title")
                .body("body")
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(notice, "user", user);

        when(noticeRepository.save(any())).thenReturn(notice);
        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));

        // when
        noticeService.update(noticeRepository, notice.getId(), notice.getUser().getId(), new RequestUpdateNoticeDto("update-title", "update-body"));

        // then
        verify(noticeRepository).save(argThat(entity -> {
            assertThat(notice.getTitle()).isEqualTo("update-title");
            assertThat(notice.getBody()).isEqualTo("update-body");
            return true;
        }));
    }

    @Test
    @DisplayName("없는 게시글 단건 조회시 오류")
    public void findOneWithException() {
        // given
        when(noticeRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () ->
                noticeService.findOne(noticeRepository, 0L, 4L, UserRole.USER));

    }
}