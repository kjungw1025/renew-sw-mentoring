package com.renew.sw.mentoring.domain.mission.model;

public enum Difficulty {
    VERY_EASY("VERY_EASY"),
    EASY("EASY"),
    NORMAL("NORMAL"),
    NORMAL_HARD("NORMAL_HARD"),
    HARD("HARD"),
    VERY_HARD("VERY_HARD");

    private String name;

    Difficulty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Difficulty of(String name) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getName().equals(name)) {
                return difficulty;
            }
        }
        return null;
    }
}
