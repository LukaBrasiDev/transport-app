package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class EditController {

    final OrderService orderService;

    @Autowired
    public EditController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("orderForm", new OrderForm());
        return "edit";
    }


    @PostMapping("/edit/{id}")
    public String register(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm) {

        orderService.updateOrder(id, orderForm);

        return "redirect:/orders";

    }



}
