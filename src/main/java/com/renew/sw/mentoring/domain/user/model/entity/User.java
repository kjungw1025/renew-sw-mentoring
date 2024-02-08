package com.renew.sw.mentoring.domain.user.model.entity;

import com.renew.sw.mentoring.domain.user.model.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user", indexes = {
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_nickname", columnList = "nickname")
})
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    // TODO : Team Entity 추가 후 주석 해제
//    private Team team;

    @NotBlank
    @Column
    private String studentId;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @NotBlank
    @Column(length = 20)
    private String name;

    @NotBlank
    @Column(length = 20)
    private String nickname;

    @Builder
    public User(@NotNull String studentId,
                @NotNull String password,
                @NotNull String name,
                @NotNull String nickname,
                UserRole userRole) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
