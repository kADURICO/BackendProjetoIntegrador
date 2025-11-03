package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Pergunta;
import com.senac.conectIdade.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Integer> {

    List<Pergunta> findByQuiz(Quiz quiz);

    List<Pergunta> findByQuizId(Integer quizId);
}