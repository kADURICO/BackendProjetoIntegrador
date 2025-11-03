package com.senac.conectIdade.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespostaQuizDtoRequest {

    @NotNull(message = "O ID da pergunta é obrigatório")
    private Integer perguntaId;

    @NotNull(message = "O ID da opção escolhida é obrigatório")
    private Integer opcaoEscolhidaId;
}