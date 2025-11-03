package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.OpcaoResposta;
import com.senac.conectIdade.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcaoRespostaRepository extends JpaRepository<OpcaoResposta, Integer> {

    List<OpcaoResposta> findByPergunta(Pergunta pergunta);

    List<OpcaoResposta> findByPerguntaId(Integer perguntaId);
}