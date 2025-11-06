package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.request.RespostaQuizDtoRequest;
import com.senac.conectIdade.dto.response.OpcaoRespostaDtoResponse;
import com.senac.conectIdade.dto.response.PerguntaDtoResponse;
import com.senac.conectIdade.dto.response.QuizDtoResponse;
import com.senac.conectIdade.dto.response.ResultadoQuizDtoResponse;
import com.senac.conectIdade.entity.*;
import com.senac.conectIdade.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final PerguntaRepository perguntaRepository;
    private final OpcaoRespostaRepository opcaoRespostaRepository;
    private final RespostaUsuarioQuizRepository respostaUsuarioQuizRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final ProgressoService progressoService;

    public QuizService(QuizRepository quizRepository, PerguntaRepository perguntaRepository,
                       OpcaoRespostaRepository opcaoRespostaRepository, RespostaUsuarioQuizRepository respostaUsuarioQuizRepository,
                       UsuarioRepository usuarioRepository, ModelMapper modelMapper, ProgressoService progressoService) {
        this.quizRepository = quizRepository;
        this.perguntaRepository = perguntaRepository;
        this.opcaoRespostaRepository = opcaoRespostaRepository;
        this.respostaUsuarioQuizRepository = respostaUsuarioQuizRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.progressoService = progressoService;
    }

    @Transactional(readOnly = true)
    public QuizDtoResponse getQuizCompleto(Integer licaoId) {
        Quiz quiz = quizRepository.findByLicaoId(licaoId)
                .orElseThrow(() -> new NoSuchElementException("Quiz não encontrado para a lição " + licaoId));

        List<Pergunta> perguntas = perguntaRepository.findByQuizId(quiz.getId());

        List<PerguntaDtoResponse> perguntaDtos = perguntas.stream().map(pergunta -> {
            PerguntaDtoResponse perguntaDto = modelMapper.map(pergunta, PerguntaDtoResponse.class);

            List<OpcaoResposta> opcoes = opcaoRespostaRepository.findByPerguntaId(pergunta.getId());
            List<OpcaoRespostaDtoResponse> opcaoDtos = opcoes.stream()
                    .map(opcao -> modelMapper.map(opcao, OpcaoRespostaDtoResponse.class))
                    .collect(Collectors.toList());

            perguntaDto.setOpcoes(opcaoDtos);
            return perguntaDto;
        }).collect(Collectors.toList());

        QuizDtoResponse quizDto = modelMapper.map(quiz, QuizDtoResponse.class);
        quizDto.setPerguntas(perguntaDtos);
        return quizDto;
    }

    @Transactional
    public ResultadoQuizDtoResponse submeterResposta(Integer usuarioId, RespostaQuizDtoRequest respostaDto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        Pergunta pergunta = perguntaRepository.findById(respostaDto.getPerguntaId())
                .orElseThrow(() -> new NoSuchElementException("Pergunta não encontrada"));
        OpcaoResposta opcaoEscolhida = opcaoRespostaRepository.findById(respostaDto.getOpcaoEscolhidaId())
                .orElseThrow(() -> new NoSuchElementException("Opção não encontrada"));

        RespostaUsuarioQuiz resposta = new RespostaUsuarioQuiz();
        resposta.setUsuario(usuario);
        resposta.setPergunta(pergunta);
        resposta.setOpcaoEscolhida(opcaoEscolhida);
        respostaUsuarioQuizRepository.save(resposta);

        boolean isCorreta = true;


        if (isCorreta) {
            progressoService.completarLicao(usuarioId, pergunta.getQuiz().getLicao().getId());
        }

        return new ResultadoQuizDtoResponse(isCorreta, pergunta.getExplicacaoResposta());
    }
}