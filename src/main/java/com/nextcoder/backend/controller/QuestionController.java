package com.nextcoder.backend.controller;

import com.nextcoder.backend.entity.*;
import com.nextcoder.backend.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin("*")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
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
}

