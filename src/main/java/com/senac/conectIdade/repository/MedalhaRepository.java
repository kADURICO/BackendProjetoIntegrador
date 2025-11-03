package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Medalha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedalhaRepository extends JpaRepository<Medalha, Integer> {

    Optional<Medalha> findByNome(String nome);
}