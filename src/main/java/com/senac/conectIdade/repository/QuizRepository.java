package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Licao;
import com.senac.conectIdade.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    Optional<Quiz> findByLicao(Licao licao);

    Optional<Quiz> findByLicaoId(Integer licaoId);
}