package com.barberia.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "estado_cita")
public class EstadoCita implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PENDIENTE = "Pendiente";
    public static final String CONFIRMADA = "Confirmada";
    public static final String CANCELADA = "Cancelada";
    public static final String COMPLETADA = "Completada";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cita")
    private Integer idEstadoCita;

    @Column(unique = true, nullable = false, length = 25)
    @NotBlank
    @Size(max = 25)
    private String nombre;
}
