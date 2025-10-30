package com.senac.conectIdade.entity;

import com.senac.conectIdade.enums.LicaoTipo; // Import the defined ENUM
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "licao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Licao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "licao_id")
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licao_modulo_id", nullable = false)
    private Modulo modulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "licao_tipo", nullable = false)
    private LicaoTipo tipo;

    @Column(name = "licao_titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "licao_pontos_recompensa", nullable = false)
    private Integer pontosRecompensa;
}