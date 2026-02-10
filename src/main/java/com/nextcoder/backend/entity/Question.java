package com.nextcoder.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "problems")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String topic;
    private String subTopic;
}

