package com.renew.sw.mentoring.domain.admin.service;

import com.renew.sw.mentoring.domain.admin.exception.AlreadyStudentIdException;
import com.renew.sw.mentoring.domain.admin.request.RequestCreateAdminDto;
import com.renew.sw.mentoring.domain.team.exception.TeamNotFoundException;
import com.renew.sw.mentoring.domain.team.model.entity.Team;
import com.renew.sw.mentoring.domain.team.repository.TeamRepository;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createAdmin(RequestCreateAdminDto dto) {
        checkAlreadyStudentId(dto.getStudentId());
        checkAlreadyNickname(dto.getNickname());
        checkAlreadyName(dto.getName());

        if (teamRepository.findByTeamName("관리자 팀").isEmpty()){
            Team team = Team.builder()
                    .teamName("관리자 팀")
                    .build();
            team.setAdminTeam();
            teamRepository.save(team);

            User user = User.builder()
                    .team(team)
                    .studentId(dto.getStudentId())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .userRole(UserRole.ADMIN)
                    .name(dto.getName())
                    .nickname(dto.getNickname())
                    .build();
            userRepository.save(user);
        } else {
            Team team = teamRepository.findByTeamName("관리자 팀").orElseThrow(TeamNotFoundException::new);
            User user = User.builder()
                    .team(team)
                    .studentId(dto.getStudentId())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .userRole(UserRole.ADMIN)
                    .name(dto.getName())
                    .nickname(dto.getNickname())
                    .build();
            userRepository.save(user);
        }
    }

    private void checkAlreadyNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new AlreadyStudentIdException();
        }
    }

    private void checkAlreadyStudentId(String studentId) {
        if (userRepository.findByStudentId(studentId).isPresent()) {
            throw new AlreadyStudentIdException();
        }
    }

    private void checkAlreadyName(String name) {
        if (userRepository.findByName(name).isPresent()) {
            throw new AlreadyStudentIdException();
        }
    }
}
