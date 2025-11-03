package com.senac.conectIdade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioMedalhaDtoResponse {

    private Integer id;
    private LocalDateTime dataConquista;
    private MedalhaDtoResponse medalha;
}