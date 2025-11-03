package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoQuizDtoResponse {

    private boolean isCorreta;
    private String explicacaoResposta;
}