package pl.lukabrasi.transportapp.calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.calculator.dto.RouteDto;
import pl.lukabrasi.transportapp.calculator.form.RouteForm;
import pl.lukabrasi.transportapp.calculator.service.RouteService;

import java.util.Optional;

@Controller
public class RouteController {

    final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/kalkulator")
    public String showCalc() {
        return "calculator";
    }


    @PostMapping("/stawki")
    public String showSettings(Model model) {
        model.addAttribute("settings", routeService.getRoutes());
        return "settings";
    }

    @PostMapping("/kalkulator")
    public String sendRoute(@ModelAttribute RouteForm routeForm,
                            Model model) {
        if (!routeForm.getRouteStr().isEmpty()) {
            model.addAttribute("routes", routeService.calculateRoute(routeForm));
            model.addAttribute("form", routeForm.getRouteStr());
        }
        return "calculator";
    }


}