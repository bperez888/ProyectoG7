package com.barberia.controller;

import com.barberia.domain.Horario;
import com.barberia.service.BarberoService;
import com.barberia.service.HorarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/horario")
public class HorarioController {

    private final HorarioService horarioService;
    private final BarberoService barberoService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var horarios = horarioService.getHorarios();
        model.addAttribute("horarios", horarios);
        model.addAttribute("totalHorarios", horarios.size());
        model.addAttribute("barberos", barberoService.getBarberos(false));
        return "horario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Horario horario, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors() || horario.getBarbero() == null
                || horario.getBarbero().getIdBarbero() == null) {
            titulo = "error";
            mensaje = "horario.error04";
        } else {
            try {
                horarioService.save(horario);
            } catch (IllegalArgumentException e) {
                titulo = "error";
                mensaje = "horario.error02";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/horario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idHorario, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            horarioService.delete(idHorario);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "horario.error01";
        } catch (Exception e) {
            titulo = "error";
            detalle = "horario.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/horario/listado";
    }
}
