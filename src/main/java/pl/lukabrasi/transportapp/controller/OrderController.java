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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class OrderController {


    final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/zlecenia")
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

    @PostMapping("/zlecenia/tydzien")
    public String getOrdersInWeek(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        String selection = rangeForm.getRadioSelect();
        if (selection.equals("allweek")) {
            Page<Order> orderPage = orderService.findCurrentWeekAll(pageable);
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
            Page<Order> orderPage = orderService.findCurrentWeekNotSold(pageable);
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

    @PostMapping("/zlecenia/zakres")
    public String findOrdersBetweenDates(@ModelAttribute RangeForm rangeForm,
                                         Model model,
                                         Pageable pageable) {
        String selection = rangeForm.getRadioSelect();
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
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
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
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
            return "order";
        }

    }

    @PostMapping("/zlecenia")
    public String createOrder(@ModelAttribute OrderForm orderForm,
                              Model model,
                              Pageable pageable) {
        OrderService.ActionResponse actionResponse = orderService.saveOrder(orderForm);
        if (actionResponse == OrderService.ActionResponse.SUCCESS) {
            Page<Order> orderPage = orderService.getOrders(pageable);
            model.addAttribute("info", actionResponse);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            return "order";//todo widok tylko dodanej tury

        }
        Page<Order> orderPage = orderService.getOrders(pageable);
        model.addAttribute("info", actionResponse);
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

    @GetMapping("/edycjazlecenie/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("orderForm", new OrderForm());
        return "edit";
    }

    @PostMapping("/edycjazlecenie/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm, Model model) {
        OrderService.ActionResponse actionResponse = orderService.updateOrder(id, orderForm);
        if (actionResponse == OrderService.ActionResponse.EDIT) {
            model.addAttribute("info", actionResponse);
            model.addAttribute("order", orderService.getOrderById(id));
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            return "edit";
        }
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "edit";
    }

    @GetMapping("/raporty")
    public String getCharts(Model model) {
        Date dt = new Date();

        List<List<Integer>> soldList = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
            soldList.add(orderService.soldByMtwInCurrentMonth(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusMonths(i)));
        }
        List<Object> lst = soldList.stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        List<LocalDate> datyRok = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
            datyRok.add(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusMonths(i));
        }
        model.addAttribute("sold", lst);
        model.addAttribute("dt", datyRok);
        return "charts";
    }

}
