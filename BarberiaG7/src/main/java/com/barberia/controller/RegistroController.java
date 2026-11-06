package com.barberia.controller;

import com.barberia.domain.Usuario;
import com.barberia.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registro")
public class RegistroController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("registro.error03", null, Locale.getDefault()));
            return "redirect:/registro/nuevo";
        }
        try {
            usuarioService.registrarCliente(usuario);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage(e.getMessage(), null, Locale.getDefault()));
            return "redirect:/registro/nuevo";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage(e.getMessage(), null, Locale.getDefault()));
            return "redirect:/registro/nuevo";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("registro.error01", null, Locale.getDefault()));
            return "redirect:/registro/nuevo";
        }
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("registro.ok", null, Locale.getDefault()));
        return "redirect:/login";
    }
}
