package com.senac.conectIdade.controller;

import com.senac.conectIdade.dto.response.LicaoDtoResponse;
import com.senac.conectIdade.dto.response.ModuloDtoResponse;
import com.senac.conectIdade.service.ModuloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @GetMapping
    public ResponseEntity<List<ModuloDtoResponse>> listarModulos() {
        List<ModuloDtoResponse> modulos = moduloService.listarModulos();
        return ResponseEntity.ok(modulos);
    }

    @GetMapping("/{id}/licoes")
    public ResponseEntity<List<LicaoDtoResponse>> listarLicoesPorModulo(@PathVariable("id") Integer moduloId) {
        List<LicaoDtoResponse> licoes = moduloService.listarLicoesPorModulo(moduloId);
        return ResponseEntity.ok(licoes);
    }
}