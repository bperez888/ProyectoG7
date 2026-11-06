package com.barberia.repository;

import com.barberia.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByCorreo(String correo);

    public boolean existsByCorreo(String correo);

    public List<Usuario> findByRolNombreOrderByApellidosAscNombreAsc(String nombreRol);

}
