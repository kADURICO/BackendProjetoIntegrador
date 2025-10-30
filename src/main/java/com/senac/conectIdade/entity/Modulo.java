package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modulo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modulo_id")
    private Integer id;

    @Column(name = "modulo_titulo", nullable = false, unique = true, length = 255)
    private String titulo;

    @Column(name = "modulo_descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "modulo_ordem", nullable = false)
    private Integer ordem;
}