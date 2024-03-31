package com.renew.sw.mentoring.domain.team.service;

import com.renew.sw.mentoring.domain.team.exception.AlreadyTeamException;
import com.renew.sw.mentoring.domain.team.exception.TeamNotFoundException;
import com.renew.sw.mentoring.domain.team.model.dto.list.SummarizedTeamDto;
import com.renew.sw.mentoring.domain.team.model.dto.response.ResponseTeamInfoDto;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /**
     * 이미 존재하는 팀인지 확인
     * @param teamName  팀 이름
     */
    public void checkAlreadyTeam(String teamName) {
        if (teamRepository.findByTeamName(teamName).isPresent()) {
            throw new AlreadyTeamException();
        }
    }

    /**
     * 전체 팀 조회 (관리자 팀 제외)
     */
    public Page<SummarizedTeamDto> getAllTeams(Pageable pageable) {
        Page<Team> teams = teamRepository.findAllDesc(pageable);
        return teams.map(team -> new SummarizedTeamDto(team, findTeamMembers(team)));
    }

    /**
     * 우리 팀 상세 조회
     *
     * @return 팀명, 팀 점수, 멘토, 멤버
     */
    public ResponseTeamInfoDto getMyTeamInfo(Long userId) {
        Team team = teamRepository.findByUserId(userId).orElseThrow(TeamNotFoundException::new);

        String mentor = userRepository.findMentorByTeamId(team.getId()).orElseThrow(TeamNotFoundException::new).getName();

        List<User> userList = userRepository.findTeamMenteeByTeamId(team.getId());

        List<String> members = new ArrayList<>();

        for (User user : userList) {
            members.add(user.getName());
        }
        return new ResponseTeamInfoDto(team, mentor, members);
    }

    private List<String> findTeamMembers(Team team) {
        String mentor = userRepository.findMentorByTeamId(team.getId()).orElseThrow(TeamNotFoundException::new).getName();
        List<User> userList = userRepository.findTeamMenteeByTeamId(team.getId());

        List<String> members = new ArrayList<>();
        members.add(mentor);
        for (User user : userList) {
            members.add(user.getName());
        }
        return members;
    }
}