package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerguntaDtoResponse {

    private Integer id;
    private String texto;
    private List<OpcaoRespostaDtoResponse> opcoes;
}