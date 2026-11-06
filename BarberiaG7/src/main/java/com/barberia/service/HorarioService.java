package com.barberia.service;

import com.barberia.domain.Horario;
import com.barberia.repository.HorarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository horarioRepository;

    @Transactional(readOnly = true)
    public List<Horario> getHorarios() {
        return horarioRepository.findAllByOrderByBarberoNombreAscDiaSemanaAscHoraInicioAsc();
    }

    @Transactional
    public void save(Horario horario) {
        if (!horario.getHoraFin().isAfter(horario.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        horarioRepository.save(horario);
    }

    @Transactional
    public void delete(Integer idHorario) {
        if (!horarioRepository.existsById(idHorario)) {
            throw new IllegalArgumentException("El horario con ID " + idHorario + " no existe.");
        }
        horarioRepository.deleteById(idHorario);
    }
}
