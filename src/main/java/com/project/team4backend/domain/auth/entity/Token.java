package com.project.team4backend.domain.auth.entity;

import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "token")
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;
}
