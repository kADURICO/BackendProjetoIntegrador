package com.senac.conectIdade.controller;

import com.senac.conectIdade.dto.request.RespostaQuizDtoRequest;
import com.senac.conectIdade.dto.response.QuizDtoResponse;
import com.senac.conectIdade.dto.response.ResultadoQuizDtoResponse;
import com.senac.conectIdade.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/licao/{licaoId}")
    public ResponseEntity<QuizDtoResponse> getQuizCompleto(@PathVariable Integer licaoId) {
        try {
            QuizDtoResponse quiz = quizService.getQuizCompleto(licaoId);
            return ResponseEntity.ok(quiz);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/submeter/usuario/{usuarioId}")
    public ResponseEntity<ResultadoQuizDtoResponse> submeterResposta(@PathVariable Integer usuarioId,
                                                                     @Valid @RequestBody RespostaQuizDtoRequest respostaDto) {
        try {
            ResultadoQuizDtoResponse resultado = quizService.submeterResposta(usuarioId, respostaDto);
            return ResponseEntity.ok(resultado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}