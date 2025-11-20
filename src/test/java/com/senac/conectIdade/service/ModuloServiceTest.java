package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.response.LicaoDtoResponse;
import com.senac.conectIdade.dto.response.ModuloDtoResponse;
import com.senac.conectIdade.entity.Licao;
import com.senac.conectIdade.entity.Modulo;
import com.senac.conectIdade.repository.LicaoRepository;
import com.senac.conectIdade.repository.ModuloRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModuloServiceTest {

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private LicaoRepository licaoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModuloService moduloService;

    @Test
    @DisplayName("Deve retornar lista de ModuloDtoResponse ordenada quando existirem modulos")
    void deveListarModulos(){
        Modulo moduloEntity = new Modulo();
        moduloEntity.setId(1);
        moduloEntity.setTitulo("Modulo 1");

        ModuloDtoResponse moduloDto = new ModuloDtoResponse();
        moduloDto.setTitulo("Modulo 1");

        // 1. Mockar o comportamento do Repositório
        when(moduloRepository.findAllByOrderByOrdemAsc())
                .thenReturn(List.of(moduloEntity));

        // 2. Mockar o comportamento do ModelMapper
        // "Quando pedir para mapear ESTA entidade para ESTA classe, retorne o DTO"
        when(modelMapper.map(moduloEntity, ModuloDtoResponse.class))
                .thenReturn(moduloDto);

        // --- ACT (Execução) ---
        List<ModuloDtoResponse> resultado = moduloService.listarModulos();

        // --- ASSERT (Validação) ---
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Modulo 1");

        verify(moduloRepository, times(1)).findAllByOrderByOrdemAsc();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver lições para o módulo")
    void deveRetornarListaVaziaQuandoModuloNaoTiverLicoes() {
        // --- ARRANGE ---
        Integer idInexistente = 999;

        // Simulamos que o banco não achou nada (retorna lista vazia)
        when(licaoRepository.findByModuloId(idInexistente))
                .thenReturn(List.of());

        // OBS: Não precisamos mockar o modelMapper aqui!
        // Como a lista vem vazia, o código nunca entra no 'map',
        // então o mapper nunca é chamado.

        // --- ACT ---
        List<LicaoDtoResponse> resultado = moduloService.listarLicoesPorModulo(idInexistente);


        // --- ASSERT ---
        assertThat(resultado).isNotNull(); // Garante que não retornou null
        assertThat(resultado).isEmpty();   // Garante que a lista está vazia

        // Verificamos se foi no banco buscar
        verify(licaoRepository, times(1)).findByModuloId(idInexistente);

        // (Opcional) Verificamos se o mapper REALMENTE não foi chamado
        verifyNoInteractions(modelMapper);
    }


}