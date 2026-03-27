package com.nextcoder.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestCaseResult {

    private String input;
    private String expected;
    private String actual;
    private String status; // PASSED / FAILED
}