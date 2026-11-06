package com.barberia.service;

import com.barberia.domain.Barbero;
import com.barberia.repository.BarberoRepository;
import com.barberia.repository.ServicioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BarberoService {

    private final BarberoRepository barberoRepository;
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Barbero> getBarberos(boolean activo) {
        if (activo) {
            return barberoRepository.findByActivoTrue();
        }
        return barberoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Barbero> getBarbero(Integer idBarbero) {
        return barberoRepository.findById(idBarbero);
    }

    @Transactional(readOnly = true)
    public List<Barbero> getBarberosPorServicio(Integer idServicio) {
        return barberoRepository.consultaPorServicio(idServicio);
    }

    @Transactional
    public void save(Barbero barbero, List<Integer> idServicios) {
        if (idServicios == null || idServicios.isEmpty()) {
            barbero.setServicios(new ArrayList<>());
        } else {
            barbero.setServicios(new ArrayList<>(servicioRepository.findAllById(idServicios)));
        }
        if (barbero.getFoto() != null && barbero.getFoto().isBlank()) {
            barbero.setFoto(null);
        }
        barberoRepository.save(barbero);
    }

    @Transactional
    public Barbero cambiarEstado(Integer idBarbero) {
        var barberoOptional = barberoRepository.findById(idBarbero);
        if (barberoOptional.isEmpty()) {
            throw new IllegalArgumentException("El barbero con ID " + idBarbero + " no existe.");
        }
        var barbero = barberoOptional.get();
        barbero.setActivo(!barbero.isActivo());
        return barberoRepository.save(barbero);
    }

    @Transactional
    public void delete(Integer idBarbero) {

        if (!barberoRepository.existsById(idBarbero)) {
            throw new IllegalArgumentException("El barbero con ID " + idBarbero + " no existe.");
        }
        try {
            barberoRepository.deleteById(idBarbero);
            barberoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el barbero. Tiene datos asociados.", e);
        }
    }
}

