package com.project.team4backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing //JPA Auditing 기능 활성화
@EnableScheduling // 스케쥴러 기능 활성화
public class Team4BackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(Team4BackEndApplication.class, args);
    }

}
