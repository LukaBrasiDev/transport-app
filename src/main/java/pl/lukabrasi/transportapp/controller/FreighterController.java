package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.form.FactoryForm;
import pl.lukabrasi.transportapp.form.FreighterForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class FreighterController {

    final OrderService orderService;

    @Autowired
    public FreighterController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/freighter")
    public String getFreighters(Model model) {

        model.addAttribute("freighters", orderService.getFreighters());

        return "freighter";
    }

    @PostMapping("/freighter")
    public String createFreighter(@ModelAttribute FreighterForm freighterForm, Model model) {
        orderService.saveFreighter(freighterForm);
        model.addAttribute("freighters", orderService.getFreighters());

        return "freighter";
    }


    @GetMapping("/freighter/{id}")
    public String getFreighterById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("freighters", orderService.getFreighterById(id));
        return "freighter";
    }

    @GetMapping("/editfreighter/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("freighters", orderService.getFreighterById(id));
        model.addAttribute("freighterForm", new FreighterForm());
        return "editfreighter";
    }


    @PostMapping("/editfreighter/{id}")
    public String updateFreighter(
            @PathVariable Long id,
            @ModelAttribute FreighterForm freighterForm) {

        orderService.updateFreighter(id, freighterForm);

        return "redirect:/freighter";

    }
}
