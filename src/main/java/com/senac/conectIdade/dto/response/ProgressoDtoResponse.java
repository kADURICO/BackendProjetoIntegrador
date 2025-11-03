package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressoDtoResponse {

    private Integer pontuacaoTotal;
    private List<Integer> licoesCompletasIds; // O frontend usará isso para mapear
    private List<UsuarioMedalhaDtoResponse> medalhas;
}