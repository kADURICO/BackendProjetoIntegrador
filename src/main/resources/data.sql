-- Remove dados antigos na ordem de dependência (Filho -> Pai)
DELETE FROM resposta_usuario_quiz;
DELETE FROM usuario_medalha;
DELETE FROM progresso_usuario;
DELETE FROM opcao_resposta;
DELETE FROM pergunta;
DELETE FROM quiz;
DELETE FROM licao;
DELETE FROM modulo;
DELETE FROM medalha;
DELETE FROM usuario;

-- 1. MODULOS
INSERT INTO modulo (modulo_titulo, modulo_descricao, modulo_ordem) VALUES
('Chamadas', 'Aprenda a fazer e receber ligações.', 1),
('Mensagens/WhatsApp', 'Aprenda a enviar mensagens de texto, áudio e fotos.', 2),
('Segurança Digital', 'Aprenda a se proteger contra golpes.', 3),
('Câmera e Fotos', 'Aprenda a usar a câmera do celular.', 4);

-- 2. MEDALHAS
INSERT INTO medalha (medalha_nome, medalha_descricao) VALUES
('Primeira Chamada', 'Concedida por completar o simulador de chamadas.'),
('Guardião Digital', 'Concedida por acertar 3 ou mais no Jogo de Segurança.'),
('Mestre das Mensagens', 'Concedida por completar o simulador de WhatsApp.'),
('Fotógrafo Expert', 'Concedida por completar o quiz da câmera.');

-- 3. LICOES
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'SIMULADOR', 'Simulador de Chamadas', 15 FROM modulo WHERE modulo_titulo = 'Chamadas';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'VIDEO', 'Vídeo: Como Fazer e Receber Chamadas', 5 FROM modulo WHERE modulo_titulo = 'Chamadas';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'QUIZ', 'Quiz: Chamadas', 10 FROM modulo WHERE modulo_titulo = 'Chamadas';

INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'SIMULADOR', 'Simulador de Mensagens (WhatsApp)', 15 FROM modulo WHERE modulo_titulo = 'Mensagens/WhatsApp';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'VIDEO', 'Vídeo: WhatsApp para Iniciantes', 5 FROM modulo WHERE modulo_titulo = 'Mensagens/WhatsApp';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'QUIZ', 'Quiz: Mensagens', 10 FROM modulo WHERE modulo_titulo = 'Mensagens/WhatsApp';

INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'SIMULADOR', 'Jogo de Segurança Digital', 15 FROM modulo WHERE modulo_titulo = 'Segurança Digital';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'VIDEO', 'Vídeo: Segurança Digital e Golpes', 5 FROM modulo WHERE modulo_titulo = 'Segurança Digital';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'QUIZ', 'Quiz: Segurança', 10 FROM modulo WHERE modulo_titulo = 'Segurança Digital';

INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'SIMULADOR', 'Simulador de Câmera', 15 FROM modulo WHERE modulo_titulo = 'Câmera e Fotos';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'VIDEO', 'Vídeo: Usando a Câmera do Celular', 5 FROM modulo WHERE modulo_titulo = 'Câmera e Fotos';
INSERT INTO licao (licao_modulo_id, licao_tipo, licao_titulo, licao_pontos_recompensa)
SELECT modulo_id, 'QUIZ', 'Quiz: Câmera', 10 FROM modulo WHERE modulo_titulo = 'Câmera e Fotos';


-- 4. QUIZ e PERGUNTAS (SEM EMOJIS)

INSERT INTO quiz (quiz_licao_id)
SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Chamadas';
INSERT INTO pergunta (pergunta_quiz_id, pergunta_texto, pergunta_explicacao_resposta)
SELECT (SELECT quiz_id FROM quiz WHERE quiz_licao_id = (SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Chamadas')),
       'Qual a função do botão vermelho durante uma chamada?',
       'O botão vermelho é usado para encerrar a ligação.';
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'A) Aumentar o volume da chamada', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'B) Encerrar a ligação', TRUE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'C) Colocar a chamada em espera', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'D) Ativar o viva-voz', FALSE;


INSERT INTO quiz (quiz_licao_id)
SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Mensagens';
INSERT INTO pergunta (pergunta_quiz_id, pergunta_texto, pergunta_explicacao_resposta)
SELECT (SELECT quiz_id FROM quiz WHERE quiz_licao_id = (SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Mensagens')),
       'Qual ícone você deve pressionar para enviar um áudio no WhatsApp?',
       'O ícone do microfone (simbolo) é usado para gravar e enviar mensagens de áudio.';
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'A) Câmera (simbolo)', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'B) Microfone (simbolo)', TRUE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'C) Clipe de papel (simbolo)', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'D) Emoji (simbolo)', FALSE;


INSERT INTO quiz (quiz_licao_id)
SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Segurança';
INSERT INTO pergunta (pergunta_quiz_id, pergunta_texto, pergunta_explicacao_resposta)
SELECT (SELECT quiz_id FROM quiz WHERE quiz_licao_id = (SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Segurança')),
       'O que você deve fazer quando receber uma mensagem pedindo seus dados bancários?',
       'NUNCA forneça seus dados bancários, senhas ou informações pessoais por mensagem.';
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'A) Enviar os dados imediatamente', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'B) Nunca fornecer dados bancários por mensagem', TRUE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'C) Enviar apenas o número da conta', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'D) Perguntar mais detalhes antes de enviar', FALSE;


INSERT INTO quiz (quiz_licao_id)
SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Câmera';
INSERT INTO pergunta (pergunta_quiz_id, pergunta_texto, pergunta_explicacao_resposta)
SELECT (SELECT quiz_id FROM quiz WHERE quiz_licao_id = (SELECT licao_id FROM licao WHERE licao_titulo = 'Quiz: Câmera')),
       'Qual ícone você deve pressionar para alternar entre câmera frontal e traseira?',
       'O ícone de rotação/troca (simbolo) permite alternar entre a câmera frontal e a traseira.';
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'A) Flash (simbolo)', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'B) Rotação/Troca (simbolo)', TRUE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'C) Timer (simbolo)', FALSE;
INSERT INTO opcao_resposta (opcao_pergunta_id, opcao_texto, opcao_is_correta)
SELECT (SELECT MAX(pergunta_id) FROM pergunta), 'D) Grade (simbolo)', FALSE;


-- 5. USUÁRIO DE TESTE
INSERT INTO usuario (usuario_google_id, usuario_nome_exibicao, usuario_email, usuario_pontuacao_total) VALUES
('123456789', 'Usuário de Teste', 'teste@gmail.com', 0);