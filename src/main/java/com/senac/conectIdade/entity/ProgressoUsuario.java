package com.senac.conectIdade.entity;

import com.senac.conectIdade.enums.ProgressoStatus; // Importando o ENUM
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "progresso_usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"progresso_usuario_id", "progresso_licao_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progresso_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progresso_usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progresso_licao_id", nullable = false)
    private Licao licao;

    @Enumerated(EnumType.STRING)
    @Column(name = "progresso_status", nullable = false)
    private ProgressoStatus status = ProgressoStatus.NAO_INICIADO;

    @Column(name = "progresso_data_conclusao")
    private LocalDateTime dataConclusao;
}