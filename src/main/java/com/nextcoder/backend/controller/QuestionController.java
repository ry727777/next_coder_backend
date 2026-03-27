package com.nextcoder.backend.controller;

import com.nextcoder.backend.entity.*;
import com.nextcoder.backend.service.QuestionService;
import org.springframework.web.bind.annotation.*;
import com.nextcoder.backend.dto.CreateQuestionRequest;
import com.nextcoder.backend.dto.QuestionResponseDto;
import com.nextcoder.backend.dto.TestCaseDto;

import java.util.List;



@RestController
@RequestMapping("/api/questions")
@CrossOrigin("*")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Question createQuestion(@RequestBody CreateQuestionRequest request) {
        return service.createQuestion(request);
    }

    @PostMapping("/testcases")
    public String createTestCases(@RequestBody List<TestCaseDto> request) {
        service.addTestCase(request);
        return "Test case added successfully";
    }
    
    

    @GetMapping
    public List<Question> getAll() {
        return service.getAll();
    }

    @GetMapping("/language/{language}")
    public List<Question> getByLanguage(@PathVariable Language language) {
        return service.getByLanguage(language);
    }

    @GetMapping("/filter")
    public List<Question> filter(
        @RequestParam Language language,
        @RequestParam String topic
    ) {
        return service.getByLanguageAndTopic(language, topic);
    }

    @GetMapping("/{id}")
    public QuestionResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

}

