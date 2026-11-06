package com.barberia.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 12, scale = 2)
    @NotNull(message = "El precio no puede estar vacío.")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser mayor o igual a 0.")
    private BigDecimal precio;

    @Column(name = "duracion_minutos", nullable = false)
    @NotNull(message = "La duración no puede estar vacía.")
    @Min(value = 1, message = "La duración debe ser de al menos 1 minuto.")
    private Integer duracionMinutos;

    @Column(nullable = false)
    private boolean activo = true;
}
