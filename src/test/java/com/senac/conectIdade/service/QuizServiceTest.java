package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.request.RespostaQuizDtoRequest;
import com.senac.conectIdade.dto.response.OpcaoRespostaDtoResponse;
import com.senac.conectIdade.dto.response.PerguntaDtoResponse;
import com.senac.conectIdade.dto.response.QuizDtoResponse;
import com.senac.conectIdade.dto.response.ResultadoQuizDtoResponse;
import com.senac.conectIdade.entity.*;
import com.senac.conectIdade.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;


import java.util.NoSuchElementException;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private PerguntaRepository perguntaRepository;
    @Mock
    private OpcaoRespostaRepository opcaoRespostaRepository;
    @Mock
    private RespostaUsuarioQuizRepository respostaUsuarioQuizRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ProgressoService progressoService;

    @InjectMocks
    private QuizService quizService;

    // ========================================================================
    // TESTES DE BUSCA DO QUIZ COMPLETO (CENÁRIO COMPLEXO)
    // ========================================================================

    @Test
    @DisplayName("Deve montar o Quiz Completo (Quiz -> Perguntas -> Opções)")
    void deveRetornarQuizCompletoComPerguntasEOpcoes() {
        // --- ARRANGE ---
        Integer licaoId = 10;
        Integer quizId = 5;
        Integer perguntaId = 100;

        // 1. Entidades do Banco
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        Pergunta pergunta = new Pergunta();
        pergunta.setId(perguntaId);

        OpcaoResposta opcao = new OpcaoResposta();
        opcao.setId(200);

        // 2. DTOs de Resposta (Usamos instâncias reais para poder setar valores)
        QuizDtoResponse quizDto = new QuizDtoResponse();
        PerguntaDtoResponse perguntaDto = new PerguntaDtoResponse();
        OpcaoRespostaDtoResponse opcaoDto = new OpcaoRespostaDtoResponse();

        // --- MOCKING (Ensinando o comportamento) ---

        // Passo 1: Achar o Quiz
        when(quizRepository.findByLicaoId(licaoId)).thenReturn(Optional.of(quiz));
        when(modelMapper.map(quiz, QuizDtoResponse.class)).thenReturn(quizDto);

        // Passo 2: Achar as Perguntas do Quiz
        when(perguntaRepository.findByQuizId(quizId)).thenReturn(List.of(pergunta));
        // Note o uso de 'eq': Quando for ESTA pergunta, retorne ESTE dto
        when(modelMapper.map(pergunta, PerguntaDtoResponse.class)).thenReturn(perguntaDto);

        // Passo 3: Achar as Opções da Pergunta
        when(opcaoRespostaRepository.findByPerguntaId(perguntaId)).thenReturn(List.of(opcao));
        when(modelMapper.map(opcao, OpcaoRespostaDtoResponse.class)).thenReturn(opcaoDto);

        // --- ACT ---
        QuizDtoResponse resultado = quizService.getQuizCompleto(licaoId);

        // --- ASSERT ---
        assertThat(resultado).isNotNull();

        // Verifica se populou a lista de perguntas
        assertThat(resultado.getPerguntas()).hasSize(1);

        // Verifica se, dentro da pergunta, populou a lista de opções
        // Essa é a parte mais importante desse teste: a aninhamento
        assertThat(resultado.getPerguntas().get(0).getOpcoes()).hasSize(1);

        verify(quizRepository).findByLicaoId(licaoId);
        verify(perguntaRepository).findByQuizId(quizId);
        verify(opcaoRespostaRepository).findByPerguntaId(perguntaId);
    }
    @Test
    @DisplayName("Deve lançar erro se não achar Quiz para a lição")
    void deveFalharSeQuizNaoExiste() {
        Integer licaoId = 99;
        when(quizRepository.findByLicaoId(licaoId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            quizService.getQuizCompleto(licaoId);
        });
    }
    // ========================================================================
    // TESTES DE SUBMISSÃO DE RESPOSTA
    // ========================================================================

    @Test
    @DisplayName("Deve salvar resposta e chamar ProgressoService")
    void deveSubmeterRespostaEAtualizarProgresso() {
        // --- ARRANGE ---
        Integer usuarioId = 1;
        Integer licaoId = 50;

        // Request
        RespostaQuizDtoRequest request = new RespostaQuizDtoRequest();
        request.setPerguntaId(10);
        request.setOpcaoEscolhidaId(20);

        // Entidades Necessárias para evitar NullPointerException
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        // Precisamos montar a cadeia: Pergunta -> Quiz -> Licao -> ID
        // Porque o seu código faz: pergunta.getQuiz().getLicao().getId()
        Licao licao = new Licao();
        licao.setId(licaoId);

        Quiz quiz = new Quiz();
        quiz.setLicao(licao);

        Pergunta pergunta = new Pergunta();
        pergunta.setId(10);
        pergunta.setQuiz(quiz); // Linkando pergunta ao quiz
        pergunta.setExplicacaoResposta("Explicacao correta");

        OpcaoResposta opcao = new OpcaoResposta();
        opcao.setId(20);

        // Mocks
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perguntaRepository.findById(request.getPerguntaId())).thenReturn(Optional.of(pergunta));
        when(opcaoRespostaRepository.findById(request.getOpcaoEscolhidaId())).thenReturn(Optional.of(opcao));

        // --- ACT ---
        ResultadoQuizDtoResponse resultado = quizService.submeterResposta(usuarioId, request);

        // --- ASSERT ---
        assertThat(resultado.isCorreta()).isTrue(); // No seu código está hardcoded true
        assertThat(resultado.getExplicacaoResposta()).isEqualTo("Explicacao correta");

        // 1. Verificamos se salvou no banco de respostas
        verify(respostaUsuarioQuizRepository).save(any(RespostaUsuarioQuiz.class));

        // 2. CRUCIAL: Verificamos se chamou o OUTRO service (Progresso)
        // Isso garante a integração entre serviços
        verify(progressoService, times(1)).completarLicao(usuarioId, licaoId);
    }

    @Test
    @DisplayName("Deve falhar ao submeter se usuário não existe")
    void deveFalharSubmissaoSemUsuario() {
        Integer usuarioId = 99;
        RespostaQuizDtoRequest request = new RespostaQuizDtoRequest();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            quizService.submeterResposta(usuarioId, request);
        });

        // Garante que não salvou nada nem chamou progresso
        verifyNoInteractions(respostaUsuarioQuizRepository);
        verifyNoInteractions(progressoService);
    }
}

