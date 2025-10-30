package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    @Column(name = "usuario_google_id", nullable = false, unique = true, length = 255)
    private String googleId; // MODIFICADO: De Integer para String (VARCHAR(255))

    @Column(name = "usuario_nome_exibicao", nullable = false, length = 255)
    private String nomeExibicao;

    @Column(name = "usuario_email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "usuario_pontuacao_total", nullable = false)
    private Integer pontuacaoTotal = 0;

    @Column(name = "usuario_config_texto_grande", nullable = false)
    private Boolean configTextoGrande = false;

    @Column(name = "usuario_config_alto_contraste", nullable = false)
    private Boolean configAltoContraste = false;

    @Column(name = "usuario_data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "usuario_ultimo_login")
    private LocalDateTime ultimoLogin;
}