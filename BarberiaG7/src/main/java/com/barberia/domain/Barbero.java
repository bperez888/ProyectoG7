package com.barberia.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "barbero")
public class Barbero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barbero")
    private Integer idBarbero;

    @Column(nullable = false, length = 75)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 75, message = "El nombre no puede tener más de 75 caracteres.")
    private String nombre;

    @Column(length = 25)
    @Size(max = 25)
    private String telefono;

    @Column(length = 1024)
    @Size(max = 1024)
    private String foto;

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToMany
    @JoinTable(name = "barbero_servicio",
            joinColumns = @JoinColumn(name = "id_barbero"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio"))
    private List<Servicio> servicios = new ArrayList<>();

    @OneToMany(mappedBy = "barbero")
    private List<Horario> horarios;
}
