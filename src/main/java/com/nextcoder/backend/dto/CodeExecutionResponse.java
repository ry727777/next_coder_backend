package com.nextcoder.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CodeExecutionResponse {

    private String output;
    private String error;
    private long executionTime;
}
