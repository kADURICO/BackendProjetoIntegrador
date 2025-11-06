package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "opcao_resposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcaoResposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opcao_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcao_pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Lob
    @Column(name = "opcao_texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "opcao_is_correta", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isCorreta;
}