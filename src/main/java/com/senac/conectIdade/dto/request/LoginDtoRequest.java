package com.senac.conectIdade.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDtoRequest {

    @NotBlank(message = "O googleId não pode ser nulo")
    private String googleId;

    @Email(message = "Email inválido")
    @NotBlank(message = "O email não pode ser nulo")
    private String email;

    @NotBlank(message = "O nome de exibição não pode ser nulo")
    private String nomeExibicao;
}