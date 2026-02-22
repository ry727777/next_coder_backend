package com.nextcoder.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nextcoder.backend.entity.TestCase;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByProblemId(Long problemId);
}