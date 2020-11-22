package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.form.FactoryForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class FactoryController {

    final OrderService orderService;
    final UserSession userSession;


    @Autowired
    public FactoryController(UserSession userSession,OrderService orderService) {
        this.userSession = userSession;
        this.orderService = orderService;
    }


    @GetMapping("/fabryki")
    public String getFactories(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("logged", userSession.getUserEntity());
        return "factory";
    }

    @PostMapping("/fabryki")
    public String createOrder(@ModelAttribute FactoryForm factoryForm, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.saveFactory(factoryForm);
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("logged", userSession.getUserEntity());
        return "factory";
    }


    @GetMapping("/fabryka/{id}")
    public String getFactoryById(@PathVariable(value = "id") Long id, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("factories", orderService.getFactoryById(id));
        model.addAttribute("logged", userSession.getUserEntity());
        return "factory";
    }

    @GetMapping("/edycjafabryka/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("factories", orderService.getFactoryById(id));
        model.addAttribute("factoryForm", new FactoryForm());
        model.addAttribute("logged", userSession.getUserEntity());
        return "editfactory";
    }


    @PostMapping("/edycjafabryka/{id}")
    public String updateFactory(
            @PathVariable Long id,
            @ModelAttribute FactoryForm factoryForm) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.updateFactory(id, factoryForm);
        return "redirect:/fabryki";

    }

}
