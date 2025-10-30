package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resposta_usuario_quiz")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespostaUsuarioQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resposta_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resposta_usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resposta_pergunta_id", nullable = false)
    private Pergunta pergunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resposta_opcao_escolhida_id", nullable = false)
    private OpcaoResposta opcaoEscolhida;

    @Column(name = "resposta_data", nullable = false, updatable = false)
    private LocalDateTime data = LocalDateTime.now(); // Espelhando o DEFAULT CURRENT_TIMESTAMP
}