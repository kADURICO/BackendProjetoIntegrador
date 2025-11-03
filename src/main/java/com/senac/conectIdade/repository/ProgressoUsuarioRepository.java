package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Licao;
import com.senac.conectIdade.entity.ProgressoUsuario;
import com.senac.conectIdade.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressoUsuarioRepository extends JpaRepository<ProgressoUsuario, Integer> {

    List<ProgressoUsuario> findByUsuario(Usuario usuario);

    List<ProgressoUsuario> findByUsuarioId(Integer usuarioId);

    Optional<ProgressoUsuario> findByUsuarioAndLicao(Usuario usuario, Licao licao);

    Optional<ProgressoUsuario> findByUsuarioIdAndLicaoId(Integer usuarioId, Integer licaoId);

    List<ProgressoUsuario> findByUsuarioIdAndLicao_ModuloId(Integer usuarioId, Integer moduloId);
}