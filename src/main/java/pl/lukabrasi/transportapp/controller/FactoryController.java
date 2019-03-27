package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.form.FactoryForm;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class FactoryController {

    final OrderService orderService;

    @Autowired
    public FactoryController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/factory")
    public String getFactories(Model model) {

        model.addAttribute("factories", orderService.getFactories());

        return "factory";
    }

    @PostMapping("/factory")
    public String createOrder(@ModelAttribute FactoryForm factoryForm, Model model) {
        orderService.saveFactory(factoryForm);
        model.addAttribute("factories", orderService.getFactories());

        return "factory";
    }


    @GetMapping("/factory/{id}")
    public String getFactoryById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("factories", orderService.getFactoryById(id));
        return "factory";
    }

    @GetMapping("/editfactory/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("factories", orderService.getFactoryById(id));
        model.addAttribute("factoryForm", new FactoryForm());
        return "editfactory";
    }


    @PostMapping("/editfactory/{id}")
    public String updateFactory(
            @PathVariable Long id,
            @ModelAttribute FactoryForm factoryForm) {

        orderService.updateFactory(id, factoryForm);

        return "redirect:/factory";

    }

}
