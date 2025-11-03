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

    @ManyToOne(fetch = FetchType.LAZY) // Muitas opções para uma pergunta
    @JoinColumn(name = "opcao_pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Lob // Para mapear o tipo TEXT (CLOB)
    @Column(name = "opcao_texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "opcao_is_correta", nullable = false)
    private Boolean isCorreta = false; // Espelhando o DEFAULT FALSE
}