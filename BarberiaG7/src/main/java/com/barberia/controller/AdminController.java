package com.barberia.controller;

import com.barberia.service.*;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CitaService citaService;
    private final BarberoService barberoService;
    private final ServicioService servicioService;
    private final UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var hoy = LocalDate.now();
        var citasHoy = citaService.getCitasPorFecha(hoy);
        var proximas = citaService.getProximasCitas();
        model.addAttribute("hoy", hoy);
        model.addAttribute("citasHoy", citasHoy);
        model.addAttribute("proximas", proximas);
        model.addAttribute("totalCitasHoy", citasHoy.size());
        model.addAttribute("totalBarberos", barberoService.getBarberos(true).size());
        model.addAttribute("totalServicios", servicioService.getServicios(true).size());
        model.addAttribute("totalClientes", usuarioService.getClientes().size());
        return "admin/dashboard";
    }
}
