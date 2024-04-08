package com.renew.sw.mentoring.domain.post.repository;

import com.renew.sw.mentoring.domain.post.model.entity.type.MissionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MissionBoardRepository extends GenericPostRepository<MissionBoard> {
    @Query("select m from MissionBoard m where m.user.id = :userId and (m.registerStatus = 'IN_PROGRESS' or m.registerStatus = 'ACCEPTED') order by m.createdAt desc ")
    Page<MissionBoard> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select m from MissionBoard m where m.registerStatus = 'IN_PROGRESS' order by m.createdAt asc ")
    Page<MissionBoard> findAllProgressPosts(Pageable pageable);

    @Query("select m from MissionBoard m where m.registerStatus = 'IN_PROGRESS' and m.user.id = :userId and m.missionId = :missionId")
    Optional<MissionBoard> findByMissionIdAndUserId(@Param("missionId") Long missionId, @Param("userId") Long userId);
}
