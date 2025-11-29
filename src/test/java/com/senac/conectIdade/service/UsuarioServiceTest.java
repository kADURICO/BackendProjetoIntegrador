package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.request.ConfiguracoesDto;
import com.senac.conectIdade.dto.request.LoginDtoRequest;
import com.senac.conectIdade.dto.response.RecoveryJwtTokenDtoResponse;
import com.senac.conectIdade.dto.response.UsuarioDtoResponse;
import com.senac.conectIdade.entity.Usuario;
import com.senac.conectIdade.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Login: Deve recuperar usuário existente e atualizar ultimo login")
    void deveLogarUsuarioExistente() {
        // --- ARRANGE ---
        String googleId = "12345";
        LoginDtoRequest loginDto = new LoginDtoRequest();
        loginDto.setGoogleId(googleId);
        loginDto.setEmail("teste@email.com");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setGoogleId(googleId);
        usuarioExistente.setUltimoLogin(LocalDateTime.now().minusDays(1)); // Data antiga

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1);
        usuarioSalvo.setUltimoLogin(LocalDateTime.now()); // Data atualizada

        UsuarioDtoResponse usuarioResponseDto = new UsuarioDtoResponse();
        usuarioResponseDto.setEmail("teste@email.com");

        // 1. O repositório ENCONTRA o usuário (Optional.of)
        when(usuarioRepository.findByGoogleId(googleId)).thenReturn(Optional.of(usuarioExistente));

        // 2. O repositório SALVA a atualização de data
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // 3. O Mapper converte
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDtoResponse.class))).thenReturn(usuarioResponseDto);

        // --- ACT ---
        RecoveryJwtTokenDtoResponse resultado = usuarioService.loginOuCriarUsuario(loginDto);

        // --- ASSERT ---
        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("mock-jwt-token-aqui"); // Verificando seu mock fixo
        assertThat(resultado.getUsuario().getEmail()).isEqualTo("teste@email.com");

        verify(usuarioRepository, times(1)).findByGoogleId(googleId);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Login: Deve criar NOVO usuário quando não existir")
    void deveCriarNovoUsuarioNoLogin() {
        // --- ARRANGE ---
        String googleId = "99999";
        LoginDtoRequest loginDto = new LoginDtoRequest();
        loginDto.setGoogleId(googleId);
        loginDto.setNomeExibicao("Novo User");
        loginDto.setEmail("novo@email.com");

        Usuario novoUsuarioSalvo = new Usuario();
        novoUsuarioSalvo.setId(10);
        novoUsuarioSalvo.setEmail("novo@email.com");

        UsuarioDtoResponse dtoResponse = new UsuarioDtoResponse();
        dtoResponse.setEmail("novo@email.com");

        // 1. O repositório NÃO encontra ninguém (Optional.empty)
        when(usuarioRepository.findByGoogleId(googleId)).thenReturn(Optional.empty());

        // 2. O repositório SALVA o novo usuário criado
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuarioSalvo);

        // 3. Mapper
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDtoResponse.class))).thenReturn(dtoResponse);

        // --- ACT ---
        RecoveryJwtTokenDtoResponse resultado = usuarioService.loginOuCriarUsuario(loginDto);

        // --- ASSERT ---
        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsuario().getEmail()).isEqualTo("novo@email.com");

        verify(usuarioRepository, times(1)).findByGoogleId(googleId);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ========================================================================
    // TESTES DE BUSCA POR ID
    // ========================================================================

    @Test
    @DisplayName("BuscarPorId: Deve retornar DTO quando usuário existe")
    void deveBuscarUsuarioPorId() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        UsuarioDtoResponse dto = new UsuarioDtoResponse();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioDtoResponse.class)).thenReturn(dto);

        UsuarioDtoResponse resultado = usuarioService.findUsuarioById(id);

        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("BuscarPorId: Deve lançar erro quando usuário não existe")
    void deveLancarErroAoBuscarIdInexistente() {
        Integer id = 99;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.findUsuarioById(id);
        });
    }

    // ========================================================================
    // TESTES DE CONFIGURAÇÕES
    // ========================================================================

    @Test
    @DisplayName("Config: Deve atualizar configurações com sucesso")
    void deveAtualizarConfiguracoes() {
        // --- ARRANGE ---
        Integer id = 1;
        ConfiguracoesDto configDto = new ConfiguracoesDto();
        configDto.setConfigTextoGrande(true);
        configDto.setConfigAltoContraste(true);

        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setConfigTextoGrande(false); // Valor antigo

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setConfigTextoGrande(true); // Valor novo

        UsuarioDtoResponse dtoResponse = new UsuarioDtoResponse();
        dtoResponse.setConfigTextoGrande(true);

        // 1. Encontra usuário
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioAntigo));

        // 2. Salva usuário modificado
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // 3. Mapeia retorno
        when(modelMapper.map(usuarioAtualizado, UsuarioDtoResponse.class)).thenReturn(dtoResponse);

        // --- ACT ---
        UsuarioDtoResponse resultado = usuarioService.atualizarConfiguracoes(id, configDto);

        // --- ASSERT ---
        assertThat(resultado.getConfigTextoGrande()).isTrue();

        // Verificamos se o método save foi chamado (confirmando a persistência)
        verify(usuarioRepository).save(usuarioAntigo);
    }

    @Test
    @DisplayName("Config: Deve falhar ao tentar atualizar usuário inexistente")
    void deveFalharAtualizarConfigSeUsuarioNaoExiste() {
        Integer id = 99;
        ConfiguracoesDto configDto = new ConfiguracoesDto();

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.atualizarConfiguracoes(id, configDto);
        });

        // Garante que NUNCA tentou salvar nada se não achou o usuário
        verify(usuarioRepository, never()).save(any());
    }
}


