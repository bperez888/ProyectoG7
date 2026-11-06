package com.barberia.controller;

import com.barberia.domain.Barbero;
import com.barberia.service.BarberoService;
import com.barberia.service.ServicioService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/barbero")
public class BarberoController {

    private final BarberoService barberoService;
    private final ServicioService servicioService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var barberos = barberoService.getBarberos(false);
        model.addAttribute("barberos", barberos);
        model.addAttribute("totalBarberos", barberos.size());
        model.addAttribute("servicios", servicioService.getServicios(false));
        return "barbero/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Barbero barbero, BindingResult bindingResult,
            @RequestParam(value = "idServicios", required = false) List<Integer> idServicios,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "barbero.error04";
        } else {
            try {
                barberoService.save(barbero, idServicios);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "barbero.error05";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/barbero/listado";
    }

    @PostMapping("/estado")
    public String estado(@RequestParam Integer idBarbero, RedirectAttributes redirectAttributes) {
        try {
            var barbero = barberoService.cambiarEstado(idBarbero);
            String mensaje = "barbero.desactivado";
            if (barbero.isActivo()) {
                mensaje = "barbero.activado";
            }
            redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage(mensaje, null, Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("barbero.error01", null, Locale.getDefault()));
        }
        return "redirect:/barbero/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idBarbero, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            barberoService.delete(idBarbero);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "barbero.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "barbero.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "barbero.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/barbero/listado";
    }
}
