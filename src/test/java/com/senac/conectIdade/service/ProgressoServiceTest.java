package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.response.ProgressoDtoResponse;
import com.senac.conectIdade.dto.response.UsuarioMedalhaDtoResponse;
import com.senac.conectIdade.entity.*;
import com.senac.conectIdade.enums.ProgressoStatus;
import com.senac.conectIdade.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressoServiceTest {


    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProgressoUsuarioRepository progressoUsuarioRepository;
    @Mock
    private UsuarioMedalhaRepository usuarioMedalhaRepository;
    @Mock
    private MedalhaRepository medalhaRepository;
    @Mock
    private LicaoRepository licaoRepository;
    @Mock
    private RespostaUsuarioQuizRepository respostaUsuarioQuizRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProgressoService progressoService;

    // ========================================================================
    // TESTES DE LEITURA (GET PROGRESSO)
    // ========================================================================

    @Test
    @DisplayName("Deve retornar progresso apenas com lições COMPLETAS")
    void deveRetornarProgressoCompletoFiltrado() {
        // --- ARRANGE ---
        Integer usuarioId = 1;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setPontuacaoTotal(500);

        // Lição 1: COMPLETA
        Licao licao1 = new Licao(); licao1.setId(10);
        ProgressoUsuario p1 = new ProgressoUsuario();
        p1.setStatus(ProgressoStatus.COMPLETO);
        p1.setLicao(licao1);

        // Lição 2: NAO_INICIADO (Não deve aparecer na lista final)
        Licao licao2 = new Licao(); licao2.setId(20);
        ProgressoUsuario p2 = new ProgressoUsuario();
        p2.setStatus(ProgressoStatus.NAO_INICIADO);
        p2.setLicao(licao2);

        UsuarioMedalha medalhaUsuario = new UsuarioMedalha();
        UsuarioMedalhaDtoResponse medalhaDto = new UsuarioMedalhaDtoResponse();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(progressoUsuarioRepository.findByUsuarioId(usuarioId)).thenReturn(List.of(p1, p2));
        when(usuarioMedalhaRepository.findByUsuarioId(usuarioId)).thenReturn(List.of(medalhaUsuario));
        when(modelMapper.map(any(), eq(UsuarioMedalhaDtoResponse.class))).thenReturn(medalhaDto);

        // --- ACT ---
        ProgressoDtoResponse resultado = progressoService.getProgressoCompleto(usuarioId);

        // --- ASSERT ---
        assertThat(resultado.getPontuacaoTotal()).isEqualTo(500);

        // O ponto crucial: A lista deve ter tamanho 1, contendo apenas a lição 10
        assertThat(resultado.getLicoesCompletasIds()).hasSize(1);
        assertThat(resultado.getLicoesCompletasIds()).contains(10);
        assertThat(resultado.getLicoesCompletasIds()).doesNotContain(20);
    }

    // ========================================================================
    // TESTES DE COMPLETAR LIÇÃO (LÓGICA DE PONTOS)
    // ========================================================================

    @Test
    @DisplayName("Deve completar lição inédita e somar pontos ao usuário")
    void deveCompletarLicaoESomarPontos() {
        // --- ARRANGE ---
        Integer usuarioId = 1;
        Integer licaoId = 5;
        Integer pontosRecompensa = 100;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setPontuacaoTotal(0);

        Licao licao = new Licao();
        licao.setId(licaoId);
        licao.setPontosRecompensa(pontosRecompensa);

        // Simulamos que NÃO existia progresso ainda (retorna empty)
        // O service vai criar um 'new ProgressoUsuario' internamente
        when(usuarioRepository.findByIdWithLock(usuarioId)).thenReturn(Optional.of(usuario));
        when(licaoRepository.findById(licaoId)).thenReturn(Optional.of(licao));
        when(progressoUsuarioRepository.findByUsuarioIdAndLicaoId(usuarioId, licaoId))
                .thenReturn(Optional.empty());

        // --- ACT ---
        progressoService.completarLicao(usuarioId, licaoId);

        // --- ASSERT ---

        // 1. Verifica se salvou o progresso como COMPLETO
        ArgumentCaptor<ProgressoUsuario> progressoCaptor = ArgumentCaptor.forClass(ProgressoUsuario.class);
        verify(progressoUsuarioRepository).save(progressoCaptor.capture());
        assertThat(progressoCaptor.getValue().getStatus()).isEqualTo(ProgressoStatus.COMPLETO);
        assertThat(progressoCaptor.getValue().getDataConclusao()).isNotNull();

        // 2. Verifica se salvou o usuário com PONTOS SOMADOS
        // Pontuação inicial (0) + Recompensa (100) = 100
        assertThat(usuario.getPontuacaoTotal()).isEqualTo(100);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("NÃO deve somar pontos se a lição JÁ estava completa")
    void naoDeveSomarPontosSeLicaoJaCompleta() {
        // --- ARRANGE ---
        Integer usuarioId = 1;
        Integer licaoId = 5;

        Usuario usuario = new Usuario();
        usuario.setPontuacaoTotal(500); // Já tinha 500 pontos

        Licao licao = new Licao();
        licao.setPontosRecompensa(100);

        ProgressoUsuario progressoExistente = new ProgressoUsuario();
        progressoExistente.setStatus(ProgressoStatus.COMPLETO); // Já está completo!

        when(usuarioRepository.findByIdWithLock(usuarioId)).thenReturn(Optional.of(usuario));
        when(licaoRepository.findById(licaoId)).thenReturn(Optional.of(licao));
        when(progressoUsuarioRepository.findByUsuarioIdAndLicaoId(usuarioId, licaoId))
                .thenReturn(Optional.of(progressoExistente));

        // --- ACT ---
        progressoService.completarLicao(usuarioId, licaoId);

        // --- ASSERT ---
        // Garante que NÃO chamou o save do repository (não alterou progresso)
        verify(progressoUsuarioRepository, never()).save(any());

        // Garante que NÃO chamou o save do usuário (não somou pontos)
        verify(usuarioRepository, never()).save(any());

        // Pontuação continua a mesma
        assertThat(usuario.getPontuacaoTotal()).isEqualTo(500);
    }

    // ========================================================================
    // TESTES DE MEDALHAS
    // ========================================================================

    @Test
    @DisplayName("Deve conceder medalha se o usuário ainda não possui")
    void deveConcederMedalhaNova() {
        // --- ARRANGE ---
        String nomeMedalha = "Iniciante";
        Usuario usuario = new Usuario();
        Medalha medalha = new Medalha();
        medalha.setNome(nomeMedalha);

        when(usuarioRepository.findByIdWithLock(1)).thenReturn(Optional.of(usuario));
        when(medalhaRepository.findByNome(nomeMedalha)).thenReturn(Optional.of(medalha));

        // Importante: Retorna empty para dizer que ele NÃO tem a medalha
        when(usuarioMedalhaRepository.findByUsuarioAndMedalha(usuario, medalha))
                .thenReturn(Optional.empty());

        // --- ACT ---
        progressoService.concederMedalha(1, nomeMedalha, 50); // 50 pontos bonus

        // --- ASSERT ---
        verify(usuarioMedalhaRepository).save(any(UsuarioMedalha.class));

        // Verifica se deu os pontos bonus
        assertThat(usuario.getPontuacaoTotal()).isEqualTo(50);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Não deve duplicar medalha se usuário já possui")
    void naoDeveDarMedalhaDuplicada() {
        Usuario usuario = new Usuario();
        Medalha medalha = new Medalha();

        when(usuarioRepository.findByIdWithLock(1)).thenReturn(Optional.of(usuario));
        when(medalhaRepository.findByNome("Ouro")).thenReturn(Optional.of(medalha));

        // SIMULA que já possui registro
        when(usuarioMedalhaRepository.findByUsuarioAndMedalha(usuario, medalha))
                .thenReturn(Optional.of(new UsuarioMedalha()));

        // --- ACT ---
        progressoService.concederMedalha(1, "Ouro", 50);

        // --- ASSERT ---
        verify(usuarioMedalhaRepository, never()).save(any()); // Não salva medalha nova
        verify(usuarioRepository, never()).save(any()); // Não dá pontos
    }

    // ========================================================================
    // TESTES DE RESET (DELETAR TUDO)
    // ========================================================================

    @Test
    @DisplayName("Deve resetar todo o progresso do usuário")
    void deveResetarProgresso() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        usuario.setPontuacaoTotal(1000);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Mocks para os finds que ocorrem dentro dos delete
        when(progressoUsuarioRepository.findByUsuarioId(id)).thenReturn(List.of());
        when(usuarioMedalhaRepository.findByUsuarioId(id)).thenReturn(List.of());
        when(respostaUsuarioQuizRepository.findByUsuarioId(id)).thenReturn(List.of());

        // --- ACT ---
        progressoService.resetarProgresso(id);

        // --- ASSERT ---
        // Verifica se chamou os deletes
        verify(progressoUsuarioRepository).deleteAll(anyList());
        verify(usuarioMedalhaRepository).deleteAll(anyList());
        verify(respostaUsuarioQuizRepository).deleteAll(anyList());

        // Verifica se zerou a pontuação
        assertThat(usuario.getPontuacaoTotal()).isEqualTo(0);
        verify(usuarioRepository).save(usuario);
    }

}