package com.renew.sw.mentoring.domain.team.service;

import com.renew.sw.mentoring.domain.team.exception.AlreadyTeamException;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void createTeam(String teamName) {
        checkAlreadyTeam(teamName);

        teamRepository.save(Team.builder().teamName(teamName).build());
    }

    public void checkAlreadyTeam(String teamName) {
        if (teamRepository.findByTeamName(teamName).isPresent()) {
            throw new AlreadyTeamException();
        }
    }
}
