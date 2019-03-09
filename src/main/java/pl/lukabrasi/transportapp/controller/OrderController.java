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

        model.addAttribute("orders",orderService.getOrders());


        return "order";
    }

   /* @GetMapping("/orders")
    public List<Order> getOrders(Model model) {
         return  orderService.getOrders();
    }*/

    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable(value = "id") Long id, Model model){
model.addAttribute("order",orderService.getOrderById(id));
       return "order";
    }


    @PostMapping("/orders")
    public String createOrder(@ModelAttribute OrderForm orderForm) {
        orderService.saveOrder(orderForm);
        return "order";
    }

}
