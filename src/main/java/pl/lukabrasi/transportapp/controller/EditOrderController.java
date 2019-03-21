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
public class EditOrderController {

    final OrderService orderService;


    @Autowired
    public EditOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("orderForm", new OrderForm());
        return "edit";
    }


    @PostMapping("/edit/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm, Model model) {

        orderService.updateOrder(id, orderForm);
        model.addAttribute("order", orderService.getOrderById(id));
        return "edit";

    }



}
