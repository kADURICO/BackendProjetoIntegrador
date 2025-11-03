package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedalhaDtoResponse {

    private Integer id;
    private String nome;
    private String descricao;
    private byte[] icone;
}