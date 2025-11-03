package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuloDtoResponse {

    private Integer id;
    private String titulo;
    private String descricao;
    private Integer ordem;
}