package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.form.RangeForm;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.service.OrderService;


@Controller
public class OrderController {


    final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model,
                            Pageable pageable) {
        Page<Order> orderPage = orderService.getOrders(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "order";
    }

    @PostMapping("/orders/range")
    public String findOrdersBetweenDates(@ModelAttribute RangeForm rangeForm,
                                         Model model,
                                         Pageable pageable) {
       String selection=  rangeForm.getRadioSelect();
        if (selection.equals("allorders")) {
            Page<Order> orderPage = orderService.getOrdersInRange(rangeForm.getDate1(), rangeForm.getDate2(), pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            return "order";
        } else {
            Page<Order> orderPage = orderService.getOrdersInRangeNotSold(rangeForm.getDate1(), rangeForm.getDate2(), pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            return "order";
        }

    }


    @PostMapping("/orders")
    public String createOrder(@ModelAttribute OrderForm orderForm,
                              Model model,
                              Pageable pageable) {
        orderService.saveOrder(orderForm);
        Page<Order> orderPage = orderService.getOrders(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "order";
    }

/*    @GetMapping("/search")
    public String showOrdersByOrderNumber(@RequestParam (value = "searchStr", required = false) String searchStr, Model model) {
        model.addAttribute("orders", orderService.getOrdersFilteredByOrderNumber(searchStr));
        return "order";
    }*/

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
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "redirect:/orders";
    }

}
