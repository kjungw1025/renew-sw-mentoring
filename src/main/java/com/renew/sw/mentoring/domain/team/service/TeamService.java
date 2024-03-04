package com.renew.sw.mentoring.domain.team.service;

import com.renew.sw.mentoring.domain.team.exception.AlreadyTeamException;
import com.renew.sw.mentoring.domain.team.model.dto.list.SummarizedTeamDto;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

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
        return teams.map(SummarizedTeamDto::new);
    }
}
