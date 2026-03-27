package com.nextcoder.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nextcoder.backend.entity.TestCase;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
        // 🔹 Get ALL test cases (for SUBMIT)
    List<TestCase> findAllByProblemId(Long problemId);

    // 🔹 Get ONLY sample test cases (for RUN)
    List<TestCase> findByProblemIdAndIsSample(Long problemId, boolean isSample);
}