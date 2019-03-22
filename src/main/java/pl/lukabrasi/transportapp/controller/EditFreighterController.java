package pl.lukabrasi.transportapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.form.FreighterForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class EditFreighterController {

    final OrderService orderService;

    public EditFreighterController(OrderService orderService) {
        this.orderService = orderService;
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
