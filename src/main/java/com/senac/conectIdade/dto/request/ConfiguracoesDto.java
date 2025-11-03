package com.senac.conectIdade.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracoesDto {

    @NotNull(message = "A configuração de texto grande é obrigatória")
    private Boolean configTextoGrande;

    @NotNull(message = "A configuração de alto contraste é obrigatória")
    private Boolean configAltoContraste;
}