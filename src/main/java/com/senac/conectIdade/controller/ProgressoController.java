package com.senac.conectIdade.controller;

import com.senac.conectIdade.dto.request.ConcederMedalhaDtoRequest;
import com.senac.conectIdade.dto.response.ProgressoDtoResponse;
import com.senac.conectIdade.service.ProgressoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/progresso/usuario/{usuarioId}")
public class ProgressoController {

    private final ProgressoService progressoService;

    public ProgressoController(ProgressoService progressoService) {
        this.progressoService = progressoService;
    }

    @GetMapping
    public ResponseEntity<ProgressoDtoResponse> getProgressoCompleto(@PathVariable Integer usuarioId) {
        try {
            ProgressoDtoResponse progresso = progressoService.getProgressoCompleto(usuarioId);
            return ResponseEntity.ok(progresso);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/completar-licao/{licaoId}")
    public ResponseEntity<Void> completarLicao(@PathVariable Integer usuarioId, @PathVariable Integer licaoId) {
        try {
            progressoService.completarLicao(usuarioId, licaoId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/conceder-medalha")
    public ResponseEntity<Void> concederMedalha(@PathVariable Integer usuarioId,
                                                @Valid @RequestBody ConcederMedalhaDtoRequest medalhaDto) {
        try {
            progressoService.concederMedalha(usuarioId, medalhaDto.getNomeMedalha(), medalhaDto.getPontosExtras());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/resetar")
    public ResponseEntity<Void> resetarProgresso(@PathVariable Integer usuarioId) {
        try {
            progressoService.resetarProgresso(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}