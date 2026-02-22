package com.nextcoder.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nextcoder.backend.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
