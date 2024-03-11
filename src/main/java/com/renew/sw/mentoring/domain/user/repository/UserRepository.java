package com.renew.sw.mentoring.domain.user.repository;

import com.renew.sw.mentoring.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.studentId = :studentId")
    Optional<User> findByStudentId(@Param("studentId") String studentId);

    @Query("select u from User u where u.nickname = :nickname")
    Optional<User> findByNickname(@Param("nickname") String nickname);

    @Query("select u from User u where u.name = :name")
    Optional<User> findByName(@Param("name") String name);

    @Query("select u from User u where u.team.id = :teamId and u.userRole = 'MENTOR'")
    Optional<User> findMentorByTeamId(@Param("teamId") Long teamId);

    @Query("select u from User u where u.team.id = :teamId and u.userRole = 'MENTEE'")
    List<User> findTeamMenteeByTeamId(@Param("teamId") Long teamId);
}
