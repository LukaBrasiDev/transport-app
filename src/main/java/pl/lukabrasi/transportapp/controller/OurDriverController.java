package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.form.OurDriverForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class OurDriverController {

    final OrderService orderService;
    final UserSession userSession;

    @Autowired
    public OurDriverController(UserSession userSession,OrderService orderService) {
        this.userSession = userSession;
        this.orderService = orderService;
    }

    @GetMapping("/kierowcy")
    public String getOurDrivers(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());
        return "ourdriver";
    }

    @PostMapping("/kierowcy")
    public String createOurDriver(@ModelAttribute OurDriverForm ourDriverForm, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.saveOurDriver(ourDriverForm);
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());
        return "ourdriver";
    }

    @GetMapping("/kierowca/{id}")
    public String getOurDriverById(@PathVariable(value = "id") Long id, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("drivers", orderService.getOurDriverById(id));
        model.addAttribute("logged", userSession.getUserEntity());
        return "ourdriver";
    }

    @GetMapping("/edycjakierowca/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("drivers", orderService.getOurDriverById(id));
        model.addAttribute("ourDriverForm", new OurDriverForm());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());
        return "editdriver";
    }

    @PostMapping("/edycjakierowca/{id}")
    public String updateOurDriver(
            @PathVariable Long id,
            @ModelAttribute OurDriverForm ourDriverForm) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.updateOurDriver(id, ourDriverForm);
        return "redirect:/kierowcy";
    }


}
