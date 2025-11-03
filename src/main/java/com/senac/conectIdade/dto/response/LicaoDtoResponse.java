package com.senac.conectIdade.dto.response;

import com.senac.conectIdade.enums.LicaoTipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicaoDtoResponse {

    private Integer id;
    private Integer moduloId;
    private LicaoTipo tipo;
    private String titulo;
    private Integer pontosRecompensa;
}