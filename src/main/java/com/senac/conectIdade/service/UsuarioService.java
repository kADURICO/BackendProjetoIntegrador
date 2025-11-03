package com.senac.conectIdade.service;

import com.senac.conectIdade.dto.request.LoginDtoRequest;
import com.senac.conectIdade.dto.response.RecoveryJwtTokenDtoResponse;
import com.senac.conectIdade.dto.response.UsuarioDtoResponse;
import com.senac.conectIdade.entity.Usuario;
import com.senac.conectIdade.repository.UsuarioRepository;
import com.senac.conectIdade.dto.request.ConfiguracoesDto;
// import com.senac.conectIdade.security.JwtTokenService;
// import com.senac.conectIdade.security.UserDetailsImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    // private final JwtTokenService jwtTokenService;


    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper/*, JwtTokenService jwtTokenService*/) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        // this.jwtTokenService = jwtTokenService;
    }


    @Transactional
    public RecoveryJwtTokenDtoResponse loginOuCriarUsuario(LoginDtoRequest loginDto) {
        Usuario usuario = usuarioRepository.findByGoogleId(loginDto.getGoogleId())
                .orElseGet(() -> criarNovoUsuario(loginDto));

        usuario.setUltimoLogin(LocalDateTime.now());
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Gera o token (exemplo, a implementação exata depende da sua config de segurança)
        // UserDetailsImpl userDetails = new UserDetailsImpl(usuarioSalvo);
        // String token = jwtTokenService.generateToken(userDetails);

        UsuarioDtoResponse usuarioDto = modelMapper.map(usuarioSalvo, UsuarioDtoResponse.class);

        // return new RecoveryJwtTokenDto(token, usuarioDto);
        // Retornando DTO mockado por enquanto:
        return new RecoveryJwtTokenDtoResponse("mock-jwt-token-aqui", usuarioDto);
    }

    private Usuario criarNovoUsuario(LoginDtoRequest loginDto) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setGoogleId(loginDto.getGoogleId());
        novoUsuario.setEmail(loginDto.getEmail());
        novoUsuario.setNomeExibicao(loginDto.getNomeExibicao());
        return novoUsuario;
    }


    public UsuarioDtoResponse findUsuarioById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        return modelMapper.map(usuario, UsuarioDtoResponse.class);
    }


    @Transactional
    public UsuarioDtoResponse atualizarConfiguracoes(Integer id, ConfiguracoesDto configDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        usuario.setConfigTextoGrande(configDto.getConfigTextoGrande());
        usuario.setConfigAltoContraste(configDto.getConfigAltoContraste());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return modelMapper.map(usuarioSalvo, UsuarioDtoResponse.class);
    }
}