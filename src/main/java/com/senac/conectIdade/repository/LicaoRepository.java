package com.senac.conectIdade.repository;

import com.senac.conectIdade.entity.Licao;
import com.senac.conectIdade.entity.Modulo;
import com.senac.conectIdade.enums.LicaoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicaoRepository extends JpaRepository<Licao, Integer> {

    List<Licao> findByModulo(Modulo modulo);

    List<Licao> findByModuloId(Integer moduloId);

    Optional<Licao> findByModuloIdAndTipo(Integer moduloId, LicaoTipo tipo);
}