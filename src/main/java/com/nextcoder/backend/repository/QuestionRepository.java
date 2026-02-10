package com.nextcoder.backend.repository;

import com.nextcoder.backend.entity.Question;
import com.nextcoder.backend.entity.Language;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByLanguage(Language language);

    List<Question> findByLanguageAndTopic(Language language, String topic);
}
