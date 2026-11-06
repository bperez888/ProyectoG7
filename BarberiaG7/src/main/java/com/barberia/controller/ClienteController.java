package com.barberia.controller;

import com.barberia.service.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
@RequestMapping("/cliente")
public class ClienteController {

    private final ServicioService servicioService;
    private final BarberoService barberoService;
    private final CitaService citaService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("servicios", servicioService.getServicios(true));
        return "cliente/servicios";
    }

    @GetMapping("/servicio/{idServicio}")
    public String servicio(@PathVariable("idServicio") Integer idServicio, Model model) {
        var servicioOptional = servicioService.getServicio(idServicio);
        if (servicioOptional.isEmpty() || !servicioOptional.get().isActivo()) {
            return "redirect:/cliente/servicios";
        }
        model.addAttribute("servicio", servicioOptional.get());
        model.addAttribute("barberos", barberoService.getBarberosPorServicio(idServicio));
        return "cliente/servicio";
    }

    @GetMapping("/reservar/{idServicio}/{idBarbero}")
    public String reservar(@PathVariable("idServicio") Integer idServicio,
            @PathVariable("idBarbero") Integer idBarbero,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {
        var servicioOptional = servicioService.getServicio(idServicio);
        var barberoOptional = barberoService.getBarbero(idBarbero);
        if (servicioOptional.isEmpty() || barberoOptional.isEmpty() || !barberoOptional.get().isActivo()) {
            return "redirect:/cliente/servicios";
        }
        model.addAttribute("servicio", servicioOptional.get());
        model.addAttribute("barbero", barberoOptional.get());
        model.addAttribute("hoy", LocalDate.now());
        if (fecha != null) {
            model.addAttribute("fecha", fecha);
            model.addAttribute("horas", citaService.getHorasDisponibles(idBarbero, idServicio, fecha));
        }
        return "cliente/reservar";
    }

    @PostMapping("/reservar/confirmar")
    public String confirmar(@RequestParam Integer idServicio, @RequestParam Integer idBarbero,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime hora,
            @RequestParam String correo, RedirectAttributes redirectAttributes) {
        try {
            var cita = citaService.reservar(correo, idServicio, idBarbero, fecha, hora);
            redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("cita.reservada", null, Locale.getDefault()));
            return "redirect:/cliente/cita/" + cita.getIdCita();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage(e.getMessage(), null, Locale.getDefault()));
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage(e.getMessage(), null, Locale.getDefault()));
        }
        return "redirect:/cliente/reservar/" + idServicio + "/" + idBarbero + "?fecha=" + fecha;
    }

    @GetMapping("/cita/{idCita}")
    public String resumen(@PathVariable("idCita") Integer idCita, Model model) {
        var citaOptional = citaService.getCita(idCita);
        if (citaOptional.isEmpty()) {
            return "redirect:/cliente/citas";
        }
        model.addAttribute("cita", citaOptional.get());
        return "cliente/resumen";
    }

    @GetMapping("/citas")
    public String citas(@RequestParam(value = "correo", required = false) String correo, Model model) {
        model.addAttribute("correo", correo);
        if (correo != null && !correo.isBlank()) {
            var usuarioOptional = usuarioService.getUsuarioPorCorreo(correo);
            if (usuarioOptional.isPresent()) {
                model.addAttribute("cliente", usuarioOptional.get());
                model.addAttribute("citas", citaService.getCitasCliente(usuarioOptional.get().getIdUsuario()));
            } else {
                model.addAttribute("error", messageSource.getMessage("cita.error01", null, Locale.getDefault()));
            }
        }
        return "cliente/citas";
    }

    @PostMapping("/citas/cancelar")
    public String cancelar(@RequestParam Integer idCita, @RequestParam String correo,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "cita.cancelada";
        try {
            citaService.cancelar(idCita, correo);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = e.getMessage();
        } catch (IllegalStateException e) {
            titulo = "error";
            mensaje = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/cliente/citas?correo=" + correo;
    }
}
