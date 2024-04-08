package com.renew.sw.mentoring.domain.mission.repository;

import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MissionRepository extends JpaRepository<Mission, Long>, JpaSpecificationExecutor<Mission> {

    Page<Mission> findAll(Specification<Mission> spec, Pageable pageable);

    @Query("select distinct m from Mission m where m.id in (select bm.mission.id from BonusMission bm where bm.mission.id = m.id)")
    Page<Mission> findAllWithBonusMission(Specification<Mission> spec, Pageable pageable);
}
