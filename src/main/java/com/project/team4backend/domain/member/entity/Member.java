package com.project.team4backend.domain.member.entity;


import com.project.team4backend.domain.member.entity.enums.Gender;
import com.project.team4backend.domain.member.entity.enums.Role;
import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {

    public boolean updateBodyInfo(LocalDate birthday, Gender gender, Double height, Double weight) {
        if (!this.birthday.equals(birthday) || !this.gender.equals(gender) || !this.height.equals(height) || !this.weight.equals(weight)) {
            this.birthday = birthday;
            this.gender = gender;
            this.height = height;
            this.weight = weight;
            return true;
        }
        return false;
    }

    public boolean updateAccountInfo(String nickname) {
        if (!this.nickname.equals(nickname)) {
            this.nickname = nickname;
            return true;
        }
        return false;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name= "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name= "height", nullable = false)
    private Double height;

    @Column(name= "weight", nullable = false)
    private Double weight;

    @Column(name= "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
