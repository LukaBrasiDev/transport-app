package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.form.OurDriverForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class OurDriverController {

    final OrderService orderService;

    @Autowired
    public OurDriverController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/kierowcy")
    public String getOurDrivers(Model model) {
        model.addAttribute("drivers", orderService.getOurDrivers());
        return "ourdriver";
    }

    @PostMapping("/kierowcy")
    public String createOurDriver(@ModelAttribute OurDriverForm ourDriverForm, Model model) {
        orderService.saveOurDriver(ourDriverForm);
        model.addAttribute("drivers", orderService.getOurDrivers());
        return "ourdriver";
    }

    @GetMapping("/kierowca/{id}")
    public String getOurDriverById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("drivers", orderService.getOurDriverById(id));
        return "ourdriver";
    }

    @GetMapping("/edycjakierowca/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("drivers", orderService.getOurDriverById(id));
        model.addAttribute("ourDriverForm", new OurDriverForm());
        return "editdriver";
    }

    @PostMapping("/edycjakierowca/{id}")
    public String updateOurDriver(
            @PathVariable Long id,
            @ModelAttribute OurDriverForm ourDriverForm) {
        orderService.updateOurDriver(id, ourDriverForm);
        return "redirect:/kierowcy";
    }


}
