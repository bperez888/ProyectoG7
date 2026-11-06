package com.barberia.repository;

import com.barberia.domain.EstadoCita;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoCitaRepository extends JpaRepository<EstadoCita, Integer> {

    public Optional<EstadoCita> findByNombre(String nombre);

}
