package com.nextcoder.backend.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "submissions")
@Data
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Question problem;

    @Column(columnDefinition = "TEXT")
    private String code;

    private String language;

    private String verdict; // AC / WA / TLE / RE

    private int totalTestCases;
    private int passedTestCases;

    private long executionTime;

    private LocalDateTime submittedAt;
}
