package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDtoResponse {

    private Integer id;
    private Integer licaoId;
    private List<PerguntaDtoResponse> perguntas;
}