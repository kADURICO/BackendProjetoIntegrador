package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_medalha", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_medalha_usuario_id", "usuario_medalha_medalha_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioMedalha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_medalha_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_medalha_usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_medalha_medalha_id", nullable = false)
    private Medalha medalha;

    @Column(name = "usuario_medalha_data_conquista", nullable = false, updatable = false)
    private LocalDateTime dataConquista = LocalDateTime.now(); // Espelhando o DEFAULT
}