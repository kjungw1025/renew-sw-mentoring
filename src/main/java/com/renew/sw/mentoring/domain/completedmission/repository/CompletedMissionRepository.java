package com.renew.sw.mentoring.domain.completedmission.repository;

import com.renew.sw.mentoring.domain.completedmission.model.entity.CompletedMission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompletedMissionRepository extends JpaRepository<CompletedMission, Long> {

    @Query("select c from CompletedMission c where c.team.id = :teamId")
    Page<CompletedMission> findAllByTeamId(Pageable pageable, @Param("teamId") Long teamId);

    @Modifying
    @Query("delete from CompletedMission c where c.team.id = :teamId and c.mission.id = :missionId ")
    void deleteByTeamIdAndMissionId(@Param("teamId") Long teamId, @Param("missionId") Long missionId);
}
