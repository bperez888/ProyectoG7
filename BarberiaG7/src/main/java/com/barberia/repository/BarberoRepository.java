package com.barberia.repository;

import com.barberia.domain.Barbero;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberoRepository extends JpaRepository<Barbero, Integer> {

    public List<Barbero> findByActivoTrue();

    @Query(value = "SELECT b FROM Barbero b JOIN b.servicios s WHERE b.activo = true AND s.idServicio = :idServicio ORDER BY b.nombre ASC")
    public List<Barbero> consultaPorServicio(@Param("idServicio") Integer idServicio);

}