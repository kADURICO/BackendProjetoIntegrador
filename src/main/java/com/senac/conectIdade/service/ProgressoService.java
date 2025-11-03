package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.response.ProgressoDtoResponse;
import com.senac.conectIdade.dto.response.UsuarioMedalhaDtoResponse;
import com.senac.conectIdade.entity.*;
import com.senac.conectIdade.enums.ProgressoStatus;
import com.senac.conectIdade.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProgressoService {

    private final UsuarioRepository usuarioRepository;
    private final ProgressoUsuarioRepository progressoUsuarioRepository;
    private final UsuarioMedalhaRepository usuarioMedalhaRepository;
    private final MedalhaRepository medalhaRepository;
    private final LicaoRepository licaoRepository;
    private final RespostaUsuarioQuizRepository respostaUsuarioQuizRepository; // Para o Reset
    private final ModelMapper modelMapper;

    public ProgressoService(UsuarioRepository usuarioRepository, ProgressoUsuarioRepository progressoUsuarioRepository,
                            UsuarioMedalhaRepository usuarioMedalhaRepository, MedalhaRepository medalhaRepository,
                            LicaoRepository licaoRepository, RespostaUsuarioQuizRepository respostaUsuarioQuizRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.progressoUsuarioRepository = progressoUsuarioRepository;
        this.usuarioMedalhaRepository = usuarioMedalhaRepository;
        this.medalhaRepository = medalhaRepository;
        this.licaoRepository = licaoRepository;
        this.respostaUsuarioQuizRepository = respostaUsuarioQuizRepository;
        this.modelMapper = modelMapper;
    }


    @Transactional(readOnly = true)
    public ProgressoDtoResponse getProgressoCompleto(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        List<ProgressoUsuario> progressos = progressoUsuarioRepository.findByUsuarioId(usuarioId);
        List<Integer> licoesCompletas = progressos.stream()
                .filter(p -> p.getStatus() == ProgressoStatus.COMPLETO)
                .map(p -> p.getLicao().getId())
                .collect(Collectors.toList());


        List<UsuarioMedalha> medalhasConquistadas = usuarioMedalhaRepository.findByUsuarioId(usuarioId);
        List<UsuarioMedalhaDtoResponse> medalhasDto = medalhasConquistadas.stream()
                .map(um -> modelMapper.map(um, UsuarioMedalhaDtoResponse.class))
                .collect(Collectors.toList());

        ProgressoDtoResponse progressoDto = new ProgressoDtoResponse();
        progressoDto.setPontuacaoTotal(usuario.getPontuacaoTotal());
        progressoDto.setLicoesCompletasIds(licoesCompletas);
        progressoDto.setMedalhas(medalhasDto);

        return progressoDto;
    }


    @Transactional
    public void completarLicao(Integer usuarioId, Integer licaoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        Licao licao = licaoRepository.findById(licaoId)
                .orElseThrow(() -> new NoSuchElementException("Lição não encontrada"));

        // Encontra ou cria o registro de progresso
        ProgressoUsuario progresso = progressoUsuarioRepository
                .findByUsuarioIdAndLicaoId(usuarioId, licaoId)
                .orElse(new ProgressoUsuario(null, usuario, licao, ProgressoStatus.NAO_INICIADO, null));

        // Atualiza o status se ainda não estiver completo
        if (progresso.getStatus() != ProgressoStatus.COMPLETO) {
            progresso.setStatus(ProgressoStatus.COMPLETO);
            progresso.setDataConclusao(LocalDateTime.now());
            progressoUsuarioRepository.save(progresso);

            // Adiciona a pontuação da lição
            addPontos(usuario, licao.getPontosRecompensa());
        }
    }


    @Transactional
    public void concederMedalha(Integer usuarioId, String nomeMedalha, Integer pontosExtras) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        Medalha medalha = medalhaRepository.findByNome(nomeMedalha)
                .orElseThrow(() -> new NoSuchElementException("Medalha '" + nomeMedalha + "' não encontrada"));

        // Verifica se o usuário já possui esta medalha
        boolean jaPossui = usuarioMedalhaRepository.findByUsuarioAndMedalha(usuario, medalha).isPresent();

        if (!jaPossui) {
            UsuarioMedalha novaConquista = new UsuarioMedalha();
            novaConquista.setUsuario(usuario);
            novaConquista.setMedalha(medalha);
            // A data é definida por padrão na entidade
            usuarioMedalhaRepository.save(novaConquista);

            // Adiciona os pontos extras da medalha
            if (pontosExtras != null && pontosExtras > 0) {
                addPontos(usuario, pontosExtras);
            }
        }
    }


    @Transactional
    public void resetarProgresso(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        progressoUsuarioRepository.deleteAll(progressoUsuarioRepository.findByUsuarioId(usuarioId));
        usuarioMedalhaRepository.deleteAll(usuarioMedalhaRepository.findByUsuarioId(usuarioId));
        respostaUsuarioQuizRepository.deleteAll(respostaUsuarioQuizRepository.findByUsuarioId(usuarioId));

        usuario.setPontuacaoTotal(0);
        usuarioRepository.save(usuario);
    }

    private void addPontos(Usuario usuario, Integer pontos) {
        if (pontos == null || pontos <= 0) return;

        usuario.setPontuacaoTotal(usuario.getPontuacaoTotal() + pontos);
        usuarioRepository.save(usuario);
    }
}