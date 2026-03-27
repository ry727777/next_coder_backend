package com.nextcoder.backend.service;

import com.nextcoder.backend.dto.*;
import com.nextcoder.backend.entity.*;
import com.nextcoder.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final TestCaseRepository testCaseRepository;

    public QuestionService(QuestionRepository repository, TestCaseRepository testCaseRepository) {
        this.repository = repository;
        this.testCaseRepository = testCaseRepository;
    }
    // save question 
    public Question createQuestion(CreateQuestionRequest request) {

        // 1️⃣ Create Question
        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());
        question.setDifficulty(Difficulty.valueOf(request.getDifficulty()));
        question.setTopic(request.getTopic());
        question.setLanguage(Language.valueOf(request.getLanguage()));

        // 2️⃣ Save Question First
        Question savedQuestion = repository.save(question);

        // 3️⃣ Save Test Cases
        if (request.getTestCases() != null) {
            for (TestCaseDto dto : request.getTestCases()) {

                TestCase testCase = new TestCase();
                testCase.setProblem(savedQuestion);
                testCase.setInputData(dto.getInputData());
                testCase.setExpectedOutput(dto.getExpectedOutput());
                testCase.setSample(dto.isSample());

                testCaseRepository.save(testCase);
            }
        }

        return savedQuestion;
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

    public QuestionResponseDto getById(Long id) {

        Question q = repository.findById(id).orElseThrow();

        List<TestCaseDto> testCases = new ArrayList<>();

        for (TestCase tc : q.getTestCases()) {

            TestCaseDto dto = new TestCaseDto();
            dto.setInputData(tc.getInputData());
            dto.setExpectedOutput(tc.getExpectedOutput());
            dto.setSample(tc.isSample());

            testCases.add(dto);
        }

        QuestionResponseDto res = new QuestionResponseDto();
        res.setId(q.getId());
        res.setTitle(q.getTitle());
        res.setDescription(q.getDescription());
        res.setTestCases(testCases);

        return res;
    }

    public void addTestCase(List<TestCaseDto> dtos) {

        List<TestCase> testCases = new ArrayList<>();

        for (TestCaseDto dto : dtos) {

            TestCase testCase = new TestCase();

            testCase.setInputData(dto.getInputData());
            testCase.setExpectedOutput(dto.getExpectedOutput());
            testCase.setSample(dto.isSample());

            Question q = new Question();
            q.setId(dto.getProblemId());

            testCase.setProblem(q);

            testCases.add(testCase);
        }

        testCaseRepository.saveAll(testCases);
    }

}
