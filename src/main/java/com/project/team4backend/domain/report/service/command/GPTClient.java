package com.project.team4backend.domain.report.service.command;

import com.project.team4backend.domain.report.dto.gpt.GPTReqDTO;
import com.project.team4backend.domain.report.dto.gpt.GPTResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GPTClient {

    @Value("${gpt.api.url}")
    private String apiUrl;

    @Value("${gpt.model}")
    private String model;

    private final RestTemplate restTemplate;

    public String generateFeedback(String prompt) {
        GPTReqDTO.GPTRequest request = new GPTReqDTO.GPTRequest(model, prompt);
        GPTResDTO response = restTemplate.postForObject(apiUrl, request, GPTResDTO.class);

        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        throw new RuntimeException("GPT 응답이 비어 있습니다.");
    }
}

