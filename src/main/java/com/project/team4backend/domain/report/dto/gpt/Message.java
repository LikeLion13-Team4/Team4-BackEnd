package com.project.team4backend.domain.report.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;     // "user", "assistant", 등
    private String content;  // 프롬프트 또는 응답
}
