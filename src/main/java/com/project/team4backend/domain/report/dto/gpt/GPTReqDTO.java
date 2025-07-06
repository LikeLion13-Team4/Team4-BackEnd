package com.project.team4backend.domain.report.dto.gpt;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

public class GPTReqDTO {
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GPTRequest {
        private String model;
        private List<Message> messages;
        private int temperature = 1;
        private int maxTokens = 500;
        private int topP = 1;
        private int frequencyPenalty = 0;
        private int presencePenalty = 0;

        public GPTRequest(String model, String prompt) {
            this.model = model;
            this.messages = List.of(new Message("user", prompt));
        }
    }

}
