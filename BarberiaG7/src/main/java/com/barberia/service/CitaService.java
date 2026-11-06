package com.barberia.service;

import com.barberia.domain.Cita;
import com.barberia.domain.EstadoCita;
import com.barberia.domain.Horario;
import com.barberia.repository.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final EstadoCitaRepository estadoCitaRepository;
    private final HorarioRepository horarioRepository;
    private final ServicioRepository servicioRepository;
    private final BarberoRepository barberoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Cita> getCitas() {
        return citaRepository.findAllByOrderByFechaDescHoraInicioDesc();
    }

    @Transactional(readOnly = true)
    public List<Cita> getCitasPorFecha(LocalDate fecha) {
        return citaRepository.consultaPorFecha(fecha);
    }

    @Transactional(readOnly = true)
    public List<Cita> getCitasCliente(Integer idUsuario) {
        return citaRepository.findByClienteIdUsuarioOrderByFechaDescHoraInicioDesc(idUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<Cita> getCita(Integer idCita) {
        return citaRepository.findById(idCita);
    }

    @Transactional(readOnly = true)
    public List<Cita> getProximasCitas() {
        return citaRepository.consultaProximas(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<EstadoCita> getEstados() {
        return estadoCitaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<LocalTime> getHorasDisponibles(Integer idBarbero, Integer idServicio, LocalDate fecha) {
        List<LocalTime> horasDisponibles = new ArrayList<>();
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            return horasDisponibles;
        }
        var servicioOptional = servicioRepository.findById(idServicio);
        if (servicioOptional.isEmpty()) {
            return horasDisponibles;
        }
        int duracion = servicioOptional.get().getDuracionMinutos();
        int diaSemana = fecha.getDayOfWeek().getValue();
        var horarios = horarioRepository.findByBarberoIdBarberoAndDiaSemanaOrderByHoraInicioAsc(idBarbero, diaSemana);
        var citas = citaRepository.findByBarberoIdBarberoAndFechaAndEstadoCitaNombreNot(idBarbero, fecha, EstadoCita.CANCELADA);

        for (Horario horario : horarios) {
            LocalTime horaInicio = horario.getHoraInicio();
            LocalTime horaFin = horaInicio.plusMinutes(duracion);
            while (!horaFin.isAfter(horario.getHoraFin()) && horaFin.isAfter(horaInicio)) {
                boolean ocupada = false;
                for (Cita cita : citas) {
                    if (horaInicio.isBefore(cita.getHoraFin()) && horaFin.isAfter(cita.getHoraInicio())) {
                        ocupada = true;
                    }
                }
                boolean yaPaso = fecha.isEqual(LocalDate.now()) && !horaInicio.isAfter(LocalTime.now());
                if (!ocupada && !yaPaso) {
                    horasDisponibles.add(horaInicio);
                }
                horaInicio = horaFin;
                horaFin = horaInicio.plusMinutes(duracion);
            }
        }
        return horasDisponibles;
    }

    @Transactional
    public Cita reservar(String correo, Integer idServicio, Integer idBarbero, LocalDate fecha, LocalTime hora) {
        var clienteOptional = usuarioRepository.findByCorreo(correo.trim().toLowerCase());
        if (clienteOptional.isEmpty()) {
            throw new IllegalArgumentException("cita.error01");
        }
        var servicioOptional = servicioRepository.findById(idServicio);
        if (servicioOptional.isEmpty() || !servicioOptional.get().isActivo()) {
            throw new IllegalArgumentException("cita.error02");
        }
        var barberoOptional = barberoRepository.findById(idBarbero);
        if (barberoOptional.isEmpty() || !barberoOptional.get().isActivo()) {
            throw new IllegalArgumentException("cita.error02");
        }
        var horasDisponibles = getHorasDisponibles(idBarbero, idServicio, fecha);
        if (hora == null || !horasDisponibles.contains(hora)) {
            throw new IllegalStateException("cita.error03");
        }
        var servicio = servicioOptional.get();
        var cita = new Cita();
        cita.setCliente(clienteOptional.get());
        cita.setServicio(servicio);
        cita.setBarbero(barberoOptional.get());
        cita.setFecha(fecha);
        cita.setHoraInicio(hora);
        cita.setHoraFin(hora.plusMinutes(servicio.getDuracionMinutos()));
        cita.setEstadoCita(getEstado(EstadoCita.PENDIENTE));
        return citaRepository.save(cita);
    }

    @Transactional
    public void cancelar(Integer idCita, String correo) {
        var citaOptional = citaRepository.findById(idCita);
        if (citaOptional.isEmpty()) {
            throw new IllegalArgumentException("cita.error04");
        }
        var cita = citaOptional.get();
        if (!cita.getCliente().getCorreo().equalsIgnoreCase(correo.trim())) {
            throw new IllegalArgumentException("cita.error04");
        }
        String estado = cita.getEstadoCita().getNombre();
        if (!estado.equals(EstadoCita.PENDIENTE) && !estado.equals(EstadoCita.CONFIRMADA)) {
            throw new IllegalStateException("cita.error05");
        }
        cita.setEstadoCita(getEstado(EstadoCita.CANCELADA));
        citaRepository.save(cita);
    }

    @Transactional
    public void actualizarEstado(Integer idCita, Integer idEstadoCita) {
        var citaOptional = citaRepository.findById(idCita);
        if (citaOptional.isEmpty()) {
            throw new IllegalArgumentException("cita.error04");
        }
        var estadoOptional = estadoCitaRepository.findById(idEstadoCita);
        if (estadoOptional.isEmpty()) {
            throw new IllegalArgumentException("cita.error06");
        }
        var cita = citaOptional.get();
        cita.setEstadoCita(estadoOptional.get());
        citaRepository.save(cita);
    }

    private EstadoCita getEstado(String nombre) {
        var estadoOptional = estadoCitaRepository.findByNombre(nombre);
        if (estadoOptional.isEmpty()) {
            throw new IllegalStateException("cita.error06");
        }
        return estadoOptional.get();
    }
}
