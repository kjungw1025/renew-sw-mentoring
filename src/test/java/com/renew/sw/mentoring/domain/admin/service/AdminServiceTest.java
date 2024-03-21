package com.renew.sw.mentoring.domain.admin.service;

import com.renew.sw.mentoring.domain.completedmission.repository.CompletedMissionRepository;
import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import com.renew.sw.mentoring.domain.mission.repository.BonusMissionRepository;
import com.renew.sw.mentoring.domain.mission.repository.MissionRepository;
import com.renew.sw.mentoring.domain.post.exception.AlreadyMissionBoardAcceptedException;
import com.renew.sw.mentoring.domain.post.exception.MissionBoardNotInProgressException;
import com.renew.sw.mentoring.domain.post.model.entity.RegisterStatus;
import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import com.renew.sw.mentoring.domain.post.repository.MissionBoardRepository;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.mock.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MissionBoardRepository missionBoardRepository;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private BonusMissionRepository bonusMissionRepository;

    @Mock
    private CompletedMissionRepository completedMissionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("미션 인증글 승인 - 메인 미션만 성공한, 승인 대기중인 글이 잘 승인 되는지?")
    void acceptMission_1() {
        // given
        Team team = TeamMock.create("팀1");
        User user = UserMock.create(team, UserRole.MENTOR, passwordEncoder);
        Mission mission = MissionMock.create();
        MissionBoard missionBoard = MissionBoardMock.create(user, mission.getId(),false, RegisterStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(missionBoard, "id", 1L);

        when(missionBoardRepository.findById(any())).thenReturn(Optional.of(missionBoard));
        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));

        // when
        adminService.acceptMission(UserRole.ADMIN, missionBoard.getId());

        // then
        assertEquals(team.getScore(), mission.getPoint());
        assertEquals(missionBoard.getRegisterStatus(), RegisterStatus.ACCEPTED);
    }

    @Test
    @DisplayName("미션 인증글 승인 - 메인, 보너스 미션 모두 성공한, 승인 대기중인 글이 잘 승인 되는지?")
    void acceptMission_2() {
        // given
        Team team = TeamMock.create("팀1");
        User user = UserMock.create(team, UserRole.MENTOR, passwordEncoder);
        Mission mission = MissionMock.create();
        BonusMission bonusMission = BonusMissionMock.create(mission);
        MissionBoard missionBoard = MissionBoardMock.create(user, mission.getId(),true, RegisterStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(missionBoard, "id", 1L);

        when(missionBoardRepository.findById(any())).thenReturn(Optional.of(missionBoard));
        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
        when(bonusMissionRepository.findAllByMissionId(any())).thenReturn(Collections.singletonList(bonusMission));

        // when
        adminService.acceptMission(UserRole.ADMIN, missionBoard.getId());

        // then
        assertEquals(team.getScore(), mission.getPoint() + bonusMission.getPoint());
        assertEquals(missionBoard.getRegisterStatus(), RegisterStatus.ACCEPTED);
    }

    @Test
    @DisplayName("미션 인증글 승인 - 승인 대기중인 글이 아닐 때, 오류 메시지를 잘 반환하는지?")
    void acceptMission_4() {
        // given
        Team team = TeamMock.create("팀1");
        User user = UserMock.create(team, UserRole.MENTOR, passwordEncoder);
        Mission mission = MissionMock.create();
        MissionBoard missionBoard = MissionBoardMock.create(user, mission.getId(),false, RegisterStatus.REJECTED);
        ReflectionTestUtils.setField(missionBoard, "id", 1L);

        when(missionBoardRepository.findById(any())).thenReturn(Optional.of(missionBoard));

        // when & then
        MissionBoardNotInProgressException exception = assertThrows(MissionBoardNotInProgressException.class, () -> adminService.acceptMission(UserRole.ADMIN, missionBoard.getId()));
        assertEquals(exception.getMessageId(), "failed.mission-board.in-progress");
    }

    @Test
    @DisplayName("미션 인증글 승인 - 이미 승인된 글일 때, 오류 메시지를 잘 반환하는지?")
    void acceptMission_5() {
        // given
        Team team = TeamMock.create("팀1");
        User user = UserMock.create(team, UserRole.MENTOR, passwordEncoder);
        Mission mission = MissionMock.create();
        MissionBoard missionBoard = MissionBoardMock.create(user, mission.getId(),false, RegisterStatus.ACCEPTED);
        ReflectionTestUtils.setField(missionBoard, "id", 1L);

        when(missionBoardRepository.findById(any())).thenReturn(Optional.of(missionBoard));

        // when & then
        AlreadyMissionBoardAcceptedException exception = assertThrows(AlreadyMissionBoardAcceptedException.class, () -> adminService.acceptMission(UserRole.ADMIN, missionBoard.getId()));
        assertEquals(exception.getMessageId(), "already.mission-board.accepted");
    }

    @Test
    @DisplayName("미션 인증 글 승인 거부가 잘 되는지?")
    void rejectMission_1() {
        // given
        Team team = TeamMock.create("팀1");
        User user = UserMock.create(team, UserRole.MENTOR, passwordEncoder);
        Mission mission = MissionMock.create();
        MissionBoard missionBoard = MissionBoardMock.create(user, mission.getId(),false, RegisterStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(missionBoard, "id", 1L);

        when(missionBoardRepository.findById(any())).thenReturn(Optional.of(missionBoard));

        // when
        adminService.rejectMission(UserRole.ADMIN, missionBoard.getId());

        // then
        assertEquals(missionBoard.getRegisterStatus(), RegisterStatus.REJECTED);
    }

    @Test
    @DisplayName("미션 인증 글 승인 거부 - 승인 대기 중인 글이 아닐 때, 오류 메시지를 잘 반환하는지?")
    void rejectMission_2() {
        // given
        Team team = TeamMock.create("팀1");
        User user = UserMock.create(team, UserRole.MENTOR, passwordEncoder);
        Mission mission = MissionMock.create();
        MissionBoard missionBoard = MissionBoardMock.create(user, mission.getId(),false, RegisterStatus.ACCEPTED);
        ReflectionTestUtils.setField(missionBoard, "id", 1L);

        when(missionBoardRepository.findById(any())).thenReturn(Optional.of(missionBoard));

        // when & then
        MissionBoardNotInProgressException exception = assertThrows(MissionBoardNotInProgressException.class, () -> adminService.rejectMission(UserRole.ADMIN, missionBoard.getId()));
        assertEquals(exception.getMessageId(), "failed.mission-board.in-progress");
    }
}