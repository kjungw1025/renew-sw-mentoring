package com.renew.sw.mentoring.domain.mission.repository;

import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
