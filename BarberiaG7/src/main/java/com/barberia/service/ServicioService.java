package com.barberia.service;

import com.barberia.domain.Servicio;
import com.barberia.repository.ServicioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Servicio> getServicios(boolean activo) {
        if (activo) {
            return servicioRepository.findByActivoTrue();
        }
        return servicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> getServicio(Integer idServicio) {
        return servicioRepository.findById(idServicio);
    }

    @Transactional
    public void save(Servicio servicio) {
        servicioRepository.save(servicio);
    }

    @Transactional
    public void delete(Integer idServicio) {

        if (!servicioRepository.existsById(idServicio)) {
            throw new IllegalArgumentException("El servicio con ID " + idServicio + " no existe.");
        }
        try {
            servicioRepository.deleteById(idServicio);
            servicioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el servicio. Tiene datos asociados.", e);
        }
    }
}
