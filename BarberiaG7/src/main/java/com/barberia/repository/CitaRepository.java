package com.barberia.repository;

import com.barberia.domain.Cita;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    public List<Cita> findAllByOrderByFechaDescHoraInicioDesc();

    public List<Cita> findByClienteIdUsuarioOrderByFechaDescHoraInicioDesc(Integer idUsuario);

    public List<Cita> findByBarberoIdBarberoAndFechaAndEstadoCitaNombreNot(Integer idBarbero, LocalDate fecha, String estado);

    @Query(value = "SELECT c FROM Cita c WHERE c.fecha >= :desde AND c.estadoCita.nombre IN ('Pendiente','Confirmada') ORDER BY c.fecha ASC, c.horaInicio ASC")
    public List<Cita> consultaProximas(@Param("desde") LocalDate desde);

    @Query(nativeQuery = true,
            value = "SELECT * FROM cita c WHERE c.fecha = :fecha ORDER BY c.hora_inicio ASC")
    public List<Cita> consultaPorFecha(@Param("fecha") LocalDate fecha);

}
