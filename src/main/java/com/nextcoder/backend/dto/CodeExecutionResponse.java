package com.nextcoder.backend.dto;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CodeExecutionResponse {
    private int total;
    private int passed;
    private String verdict;
    private List<TestCaseResult> results;
}
