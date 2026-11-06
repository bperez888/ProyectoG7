package com.barberia.controller;

import com.barberia.domain.Cita;
import com.barberia.service.CitaService;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cita")
public class CitaController {

    private final CitaService citaService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(@RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, Model model) {
        List<Cita> citas;
        if (fecha == null) {
            citas = citaService.getCitas();
        } else {
            citas = citaService.getCitasPorFecha(fecha);
        }
        model.addAttribute("citas", citas);
        model.addAttribute("totalCitas", citas.size());
        model.addAttribute("fecha", fecha);
        model.addAttribute("estados", citaService.getEstados());
        return "cita/listado";
    }

    @PostMapping("/estado")
    public String estado(@RequestParam Integer idCita, @RequestParam Integer idEstadoCita,
            @RequestParam(value = "fecha", required = false) String fecha,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "cita.estado.ok";
        try {
            citaService.actualizarEstado(idCita, idEstadoCita);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        if (fecha != null && !fecha.isBlank()) {
            return "redirect:/cita/listado?fecha=" + fecha;
        }
        return "redirect:/cita/listado";
    }
}
