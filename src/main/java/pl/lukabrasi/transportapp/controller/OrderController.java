package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.service.OrderService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {


    final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {

        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("users", orderService.getUsers());

        return "order";
    }

    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("orders", orderService.getOrderById(id));
        return "order";
    }


    @PostMapping("/orders")
    public String createOrder(@ModelAttribute OrderForm orderForm, Model model) {
        orderService.saveOrder(orderForm);
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("users", orderService.getUsers());
        return "order";
    }

}
