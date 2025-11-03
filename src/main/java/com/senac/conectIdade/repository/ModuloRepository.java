package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Integer> {

    List<Modulo> findAllByOrderByOrdemAsc();

    Optional<Modulo> findByTitulo(String titulo);
}