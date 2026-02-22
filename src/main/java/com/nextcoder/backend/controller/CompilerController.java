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

    @PostMapping("/run")
    public CodeExecutionResponse run(@RequestBody CodeExecutionRequest request) {
        return service.execute(request);
    }
}
