package com.nextcoder.backend.controller;

import com.nextcoder.backend.dto.*;
import com.nextcoder.backend.service.ExecutionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compiler")
@CrossOrigin("*")
public class CompilerController {

    private final ExecutionService service;

    public CompilerController(ExecutionService service) {
        this.service = service;
    }

    // 🔹 Run code (sample test cases only)
    @PostMapping("/run")
    public CodeExecutionResponse run(@RequestBody CodeExecutionRequest request) {
         if (request.getCode() == null || request.getCode().isEmpty()) {
                throw new RuntimeException("Code cannot be empty");
            }
        return service.execute(request, true);
    }

    // 🔹 Submit code (all test cases)
    @PostMapping("/submit")
    public CodeExecutionResponse submit(@RequestBody CodeExecutionRequest request) {
        return service.execute(request, false);
    }
}
