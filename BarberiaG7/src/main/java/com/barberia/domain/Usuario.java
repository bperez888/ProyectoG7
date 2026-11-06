package com.barberia.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    @Column(nullable = false, length = 75)
    @NotBlank(message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 75, message = "Los apellidos no pueden tener más de 75 caracteres.")
    private String apellidos;

    @Column(unique = true, nullable = false, length = 75)
    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "El correo no tiene un formato válido.")
    @Size(max = 75)
    private String correo;

    @Column(length = 25)
    @Size(max = 25)
    private String telefono;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(max = 512)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}
