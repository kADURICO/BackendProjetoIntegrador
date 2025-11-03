package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pergunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pergunta_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitas perguntas para um quiz
    @JoinColumn(name = "pergunta_quiz_id", nullable = false)
    private Quiz quiz;

    @Lob // Para mapear o tipo TEXT (CLOB)
    @Column(name = "pergunta_texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Lob // Para mapear o tipo TEXT (CLOB)
    @Column(name = "pergunta_explicacao_resposta", columnDefinition = "TEXT")
    private String explicacaoResposta;
}