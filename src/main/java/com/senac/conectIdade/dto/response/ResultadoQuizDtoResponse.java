package com.senac.conectIdade.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoQuizDtoResponse {

    @JsonProperty("isCorreta")
    private boolean isCorreta;
    private String explicacaoResposta;
}