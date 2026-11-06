package com.barberia.repository;

import com.barberia.domain.Horario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    public List<Horario> findAllByOrderByBarberoNombreAscDiaSemanaAscHoraInicioAsc();

    public List<Horario> findByBarberoIdBarberoAndDiaSemanaOrderByHoraInicioAsc(Integer idBarbero, Integer diaSemana);

}
