package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.service.OrderService;


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
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("cities", orderService.getCities());

        return "order";
    }

    @PostMapping("/orders")
    public String createOrder(@ModelAttribute OrderForm orderForm, Model model) {
        orderService.saveOrder(orderForm);
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("cities", orderService.getCities());
        model.addAttribute("codes", orderService.getCodes());
        return "order";
    }

    @GetMapping("/search")
    public String showOrdersByOrderNumber(@RequestParam (value = "searchStr", required = false) String searchStr, Model model) {
        model.addAttribute("orders", orderService.getOrdersFilteredByOrderNumber(searchStr));
        return "order";
    }

}
