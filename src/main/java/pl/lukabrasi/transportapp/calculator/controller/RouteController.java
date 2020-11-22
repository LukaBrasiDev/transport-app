package pl.lukabrasi.transportapp.calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.calculator.dto.RouteDto;
import pl.lukabrasi.transportapp.calculator.form.CityForm;
import pl.lukabrasi.transportapp.calculator.form.RouteForm;
import pl.lukabrasi.transportapp.calculator.service.RouteService;

import java.util.Optional;

@Controller
public class RouteController {

    final RouteService routeService;
    final UserSession userSession;

    @Autowired
    public RouteController(UserSession userSession,RouteService routeService) {
        this.userSession = userSession;
        this.routeService = routeService;
    }

    @GetMapping("/kalkulator")
    public String showCalc(Model model) {

        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("logged", userSession.getUserEntity());
        return "calculator";
    }


    @PostMapping("/stawki")
    public String showSettings(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("settings", routeService.getRoutes());
        return "settings";
    }

    @GetMapping("/stawki")
    public String showSettings2(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("settings", routeService.getRoutes());
        return "settings";
    }

    @PostMapping("/kalkulator")
    public String sendRoute(@ModelAttribute RouteForm routeForm,
                            Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        if (!routeForm.getRouteStr().isEmpty()) {
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("routes", routeService.calculateRoute(routeForm));
            model.addAttribute("form", routeForm.getRouteStr());
        }
        return "calculator";
    }
    @PostMapping("/nowemiasto")
    public String sendRoute(@ModelAttribute CityForm cityForm,
                            Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        routeService.saveCity(cityForm);
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("settings", routeService.getRoutes());
              return "settings";
    }

    @GetMapping("/edycjamiasto/{id}")
    public String editcity(@PathVariable Long id,
                       Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("settings", routeService.getRouteById(id));
         return "editsettings";
    }

    @PostMapping("/edycjamiasto/{id}")
    public String updateCity(
            @PathVariable Long id,
            @ModelAttribute CityForm cityForm) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        routeService.updateCity(id, cityForm);
        return "redirect:/stawki";
    }

}