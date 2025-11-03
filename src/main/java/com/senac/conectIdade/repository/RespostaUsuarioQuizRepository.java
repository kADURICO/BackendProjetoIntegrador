package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Pergunta;
import com.senac.conectIdade.entity.RespostaUsuarioQuiz;
import com.senac.conectIdade.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RespostaUsuarioQuizRepository extends JpaRepository<RespostaUsuarioQuiz, Integer> {

    List<RespostaUsuarioQuiz> findByUsuarioId(Integer usuarioId);

    List<RespostaUsuarioQuiz> findByUsuario(Usuario usuario);

    Optional<RespostaUsuarioQuiz> findByUsuarioAndPergunta(Usuario usuario, Pergunta pergunta);

    Optional<RespostaUsuarioQuiz> findByUsuarioIdAndPerguntaId(Integer usuarioId, Integer perguntaId);
}