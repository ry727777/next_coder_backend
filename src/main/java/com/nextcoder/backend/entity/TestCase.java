package com.nextcoder.backend.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "test_cases")
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Question problem;

    @Column(columnDefinition = "TEXT")
    private String inputData;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;

    private boolean isSample;
}
