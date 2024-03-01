package com.renew.sw.mentoring.domain.mission.repository;

import com.renew.sw.mentoring.domain.mission.model.Difficulty;
import com.renew.sw.mentoring.domain.mission.model.entity.Mission;
import org.springframework.data.jpa.domain.Specification;

public class MissionSpec {

    public static Specification<Mission> withKeyword(String keyword) {
        if (keyword == null || keyword.equals("null")) {
            return Specification.where(null);
        }

        String pattern = "%" + keyword + "%";
        return (root, query, builder) ->
                builder.like(root.get("name"), pattern);
    }

    public static Specification<Mission> withDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            return Specification.where(null);
        }

        return (root, query, builder) ->
                builder.equal(root.get("difficulty"), difficulty);
    }
}
