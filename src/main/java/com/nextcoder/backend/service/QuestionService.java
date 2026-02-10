package com.nextcoder.backend.service;

import com.nextcoder.backend.entity.*;
import com.nextcoder.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getAll() {
        return repository.findAll();
    }

    public List<Question> getByLanguage(Language language) {
        return repository.findByLanguage(language);
    }

    public List<Question> getByLanguageAndTopic(Language language, String topic) {
        return repository.findByLanguageAndTopic(language, topic);
    }

    public Question saveQuestion(Question q) {
        return repository.save(q);
    }
}
