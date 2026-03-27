package com.nextcoder.backend.dto;

import java.util.List;
import lombok.Data;

@Data
public class QuestionResponseDto {

    private Long id;
    private String title;
    private String description;

    private List<TestCaseDto> testCases;

}