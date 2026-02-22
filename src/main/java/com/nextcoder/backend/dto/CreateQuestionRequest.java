package com.nextcoder.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateQuestionRequest {

    private String title;
    private String description;
    private String difficulty;
    private String topic;
    private String language;
    private List<TestCaseDto> testCases;
}