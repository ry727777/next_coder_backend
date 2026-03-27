package com.nextcoder.backend.dto;

import lombok.Data;

@Data
public class TestCaseDto {

    private String inputData;
    private String expectedOutput;
    private boolean sample;
    private Long problemId; 
}