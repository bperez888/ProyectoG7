package com.barberia.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "horario")
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    @ManyToOne
    @JoinColumn(name = "id_barbero")
    private Barbero barbero;

    @Column(name = "dia_semana", nullable = false)
    @NotNull(message = "Debe seleccionar el día de la semana.")
    @Min(1)
    @Max(7)
    private Integer diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    @NotNull(message = "La hora de inicio no puede estar vacía.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    @NotNull(message = "La hora de fin no puede estar vacía.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFin;
}
