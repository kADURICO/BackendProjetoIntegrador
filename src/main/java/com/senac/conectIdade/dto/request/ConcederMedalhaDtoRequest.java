package com.senac.conectIdade.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcederMedalhaDtoRequest {

    @NotBlank(message = "O nome da medalha não pode ser nulo")
    private String nomeMedalha;

    private Integer pontosExtras;
}