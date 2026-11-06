package com.barberia.service;

import com.barberia.domain.Usuario;
import com.barberia.repository.RolRepository;
import com.barberia.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    public static final String ROL_CLIENTE = "Cliente";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Transactional
    public Usuario registrarCliente(Usuario usuario) {
        usuario.setCorreo(usuario.getCorreo().trim().toLowerCase());

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalStateException("registro.error01");
        }
        var rolOptional = rolRepository.findByNombre(ROL_CLIENTE);
        if (rolOptional.isEmpty()) {
            throw new IllegalArgumentException("registro.error02");
        }
        usuario.setIdUsuario(null);
        usuario.setRol(rolOptional.get());
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> getClientes() {
        return usuarioRepository.findByRolNombreOrderByApellidosAscNombreAsc(ROL_CLIENTE);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return Optional.empty();
        }
        return usuarioRepository.findByCorreo(correo.trim().toLowerCase());
    }
}
