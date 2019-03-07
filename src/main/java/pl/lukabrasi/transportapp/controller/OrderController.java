package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.service.OrderService;

import java.util.List;

@Controller
public class OrderController {


    final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {

        model.addAttribute("orders",orderService.getOrders());
        return "order";

    }

}
