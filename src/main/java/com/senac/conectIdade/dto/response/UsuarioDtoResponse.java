package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDtoResponse {

    private Integer id;
    private String nomeExibicao;
    private String email;
    private Integer pontuacaoTotal;
    private Boolean configTextoGrande;
    private Boolean configAltoContraste;
}