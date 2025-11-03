package com.senac.conectIdade.controller;

import com.senac.conectIdade.dto.request.ConfiguracoesDto;
import com.senac.conectIdade.dto.request.LoginDtoRequest;
import com.senac.conectIdade.dto.response.RecoveryJwtTokenDtoResponse;
import com.senac.conectIdade.dto.response.UsuarioDtoResponse;
import com.senac.conectIdade.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RecoveryJwtTokenDtoResponse> loginOuCriarUsuario(@Valid @RequestBody LoginDtoRequest loginDto) {
        RecoveryJwtTokenDtoResponse tokenDto = usuarioService.loginOuCriarUsuario(loginDto);
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> getUsuarioById(@PathVariable Integer id) {
        try {
            UsuarioDtoResponse usuarioDto = usuarioService.findUsuarioById(id);
            return ResponseEntity.ok(usuarioDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/configuracoes")
    public ResponseEntity<UsuarioDtoResponse> atualizarConfiguracoes(@PathVariable Integer id,
                                                             @Valid @RequestBody ConfiguracoesDto configDto) {
        try {
            UsuarioDtoResponse usuarioAtualizado = usuarioService.atualizarConfiguracoes(id, configDto);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}