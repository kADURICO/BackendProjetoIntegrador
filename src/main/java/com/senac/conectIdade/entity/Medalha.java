package com.senac.conectIdade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medalha")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medalha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medalha_id")
    private Integer id;

    @Column(name = "medalha_nome", nullable = false, unique = true, length = 255)
    private String nome;

    @Lob // Mantido para mapear TEXT (CLOB)
    @Column(name = "medalha_descricao", nullable = false)
    private String descricao;

    @Lob // Mantido para mapear BLOB
    @Column(name = "medalha_icone") // Nullable por padrão, o que corresponde ao SQL
    private byte[] icone;
}