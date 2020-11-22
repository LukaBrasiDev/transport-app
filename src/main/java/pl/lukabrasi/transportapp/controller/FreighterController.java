package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.form.FreighterForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class FreighterController {

    final OrderService orderService;
    final UserSession userSession;

    @Autowired
    public FreighterController(UserSession userSession,OrderService orderService) {
        this.userSession = userSession;
        this.orderService = orderService;
    }


    @GetMapping("/przewoznicy")
    public String getFreighters(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "freighter";
    }

    @PostMapping("/przewoznicy")
    public String createFreighter(@ModelAttribute FreighterForm freighterForm, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.saveFreighter(freighterForm);
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "freighter";
    }


    @GetMapping("/przewoznik/{id}")
    public String getFreighterById(@PathVariable(value = "id") Long id, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("freighters", orderService.getFreighterById(id));
        model.addAttribute("logged", userSession.getUserEntity());
        return "freighter";
    }

    @GetMapping("/edycjaprzewoznik/{id}")
    public String edit(@PathVariable Long id, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("freighters", orderService.getFreighterById(id));
        model.addAttribute("freighterForm", new FreighterForm());
        model.addAttribute("logged", userSession.getUserEntity());
        return "editfreighter";
    }

    @PostMapping("/edycjaprzewoznik/{id}")
    public String updateFreighter(
            @PathVariable Long id,
            @ModelAttribute FreighterForm freighterForm) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.updateFreighter(id, freighterForm);
        return "redirect:/przewoznicy";
    }
}
