package com.renew.sw.mentoring.domain.mission.repository;

import com.renew.sw.mentoring.domain.mission.model.entity.BonusMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BonusMissionRepository extends JpaRepository<BonusMission, Long> {

    @Query("select bm from BonusMission bm where bm.mission.id = :missionId")
    List<BonusMission> findAllByMissionId(@Param("missionId") Long missionId);


}
