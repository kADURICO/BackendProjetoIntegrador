package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Medalha;
import com.senac.conectIdade.entity.Usuario;
import com.senac.conectIdade.entity.UsuarioMedalha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioMedalhaRepository extends JpaRepository<UsuarioMedalha, Integer> {

    List<UsuarioMedalha> findByUsuario(Usuario usuario);

    List<UsuarioMedalha> findByUsuarioId(Integer usuarioId);

    Optional<UsuarioMedalha> findByUsuarioAndMedalha(Usuario usuario, Medalha medalha);
}