package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.form.MonthForm;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.form.RangeForm;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.service.OrderService;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
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

    @GetMapping("/naszeauta")
    public String getOurCars(Model model,
                            Pageable pageable) {
        Page<Order> orderPage = orderService.findAllMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "import";
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

    @GetMapping("/naszeauta/tydzien")
    public String getOrdersInWeekMTW(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
            Page<Order> orderPage = orderService.findCurrentWeekMTW(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            return "import";

    }

    @PostMapping("/zlecenia/tydzien/poprzedni")
    public String getOrdersInPreviousWeek(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        String selection = rangeForm.getRadioSelect();
        if (selection.equals("allweek")) {
            Page<Order> orderPage = orderService.findPreviousWeekAll(pageable);
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
            Page<Order> orderPage = orderService.findPreviousWeekNotSold(pageable);
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

    @GetMapping("/naszeauta/tydzien/poprzedni")
    public String getOrdersPreviousWeekMTWonly(
                                     Model model,
                                     Pageable pageable) {
        Page<Order> orderPage = orderService.findPreviousWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "import";

    }

    @GetMapping("/naszeauta/tydzien/2poprzednie")
    public String getOrdersTwoPreviousWeekMTW(Model model,
                                           Pageable pageable) {
        Page<Order> orderPage = orderService.find2PreviousWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "import";

    }

    @GetMapping("/naszeauta/tydzien/3poprzednie")
    public String getOrders3PreviousWeekMTW(Model model,
                                              Pageable pageable) {
        Page<Order> orderPage = orderService.find3PreviousWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "import";

    }

    @PostMapping("/zlecenia/tydzien/nastepny")
    public String getOrdersInNextWeek(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        String selection = rangeForm.getRadioSelect();
        if (selection.equals("allweek")) {
            Page<Order> orderPage = orderService.findNextWeekAll(pageable);
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
            Page<Order> orderPage = orderService.findNextWeekNotSold(pageable);
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

    @GetMapping("/naszeauta/tydzien/nastepny")
    public String getOrdersNextWeekMTW(@ModelAttribute RangeForm rangeForm,
                                     Model model,
                                     Pageable pageable) {
        Page<Order> orderPage = orderService.findNextWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        return "import";

    }
    @PostMapping("/zlecenia/zakres")
    public String findOrdersBetweenDates(@ModelAttribute RangeForm rangeForm,
                                         Model model) {
        String selection = rangeForm.getRadioSelect();
        if (selection.equals("allorders")) {
           List<Order> orderPage = orderService.getOrdersInRange(rangeForm.getDate1(), rangeForm.getDate2());
            model.addAttribute("orders", orderPage);
            model.addAttribute("totalPages", -1);
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
            return "order";
        } else {
            List<Order> orderPage = orderService.getOrdersInRangeNotSold(rangeForm.getDate1(), rangeForm.getDate2());
            model.addAttribute("orders", orderPage);
            model.addAttribute("totalPages", -1);
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
            return "order";
        }

    }

    @PostMapping("/naszeauta/zakres")
    public String findMTWOrdersBetweenDates(@ModelAttribute RangeForm rangeForm,
                                         Model model) {
            List<Order> orderPage = orderService.findMTWOrdersInRange(rangeForm.getDate1(), rangeForm.getDate2());
            model.addAttribute("orders", orderPage);
            model.addAttribute("totalPages", -1);
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
            return "import";

        }

    @GetMapping("/zlecenia/tura")
    public String findOrderNumber(@RequestParam String searchStr,
                                         Model model,
                                         Pageable pageable) {

            Page<Order> orderPage = orderService.findAllByOrderNumberContains(searchStr, pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
          //  model.addAttribute("range1", rangeForm.getDate1());
          //  model.addAttribute("range2", rangeForm.getDate2());
            return "order";


    }


    @PostMapping("/zlecenia")
    public String createOrder(@ModelAttribute OrderForm orderForm,
                              Model model,
                              Pageable pageable) throws UnknownHostException {

        OrderService.ActionResponse actionResponse = orderService.saveOrder(orderForm);
        if (actionResponse == OrderService.ActionResponse.SUCCESS) {
            Page<Order> orderPage = orderService.getOrderById(pageable);
            model.addAttribute("info", actionResponse);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            // model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("orders", orderService.getOrderById(orderPage.getContent().get(0).getId()));
            return "order";

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


    @GetMapping("/edycjazlecenie/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
       model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("freightersasc", orderService.getFreightersSorted());
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("orderForm", new OrderForm());
        return "edit";
    }



    @PostMapping("/edycjazlecenie/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm, Model model) throws UnknownHostException {
        OrderService.ActionResponse actionResponse = orderService.updateOrder(id, orderForm);

        model.addAttribute("info", actionResponse);
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("drivers", orderService.getOurDrivers());
        return "edit";
    }

    @GetMapping("/edycjaimport/{id}")
    public String editImport(@PathVariable Long id,
                             Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("freightersasc", orderService.getFreightersSorted());
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("orderForm", new OrderForm());
        return "editimport";
    }

    @PostMapping("/edycjaimport/{id}")
    public String updateImport(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm, Model model) throws UnknownHostException {
        OrderService.ActionResponse actionResponse = orderService.updateOrderImport(id, orderForm);

        model.addAttribute("info", actionResponse);
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("drivers", orderService.getOurDrivers());
        return "editimport";
    }

    @GetMapping("/raporty/wykresy")
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

        Calendar cal = Calendar.getInstance();
        List<Integer> datyWeek = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
           int week = cal.get(Calendar.WEEK_OF_YEAR)+i;
            if (week > 0){
                datyWeek.add(week);
            }
            if (week==0){
                datyWeek.add(53);
            }
            if (week<0){
                datyWeek.add(53+week);
            }

        }
        model.addAttribute("dtW", datyWeek);

        // tygodnie sprzedazy Bega wstecz
        List<Integer> soldBegaWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldBegaWeeklyList.add(
                    orderService.getBegaWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyBega",soldBegaWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldWojcikWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldWojcikWeeklyList.add(
                   orderService.getWojcikWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
       model.addAttribute("weeklyWojcik",soldWojcikWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);
        model.addAttribute("users", orderService.getUsers());

        return "charts";
    }

    @GetMapping("/raporty/spedytorzy")
    public String getChartsSpedytors (Model model) {
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

        Calendar cal = Calendar.getInstance();
        List<Integer> datyWeek = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
            int week = cal.get(Calendar.WEEK_OF_YEAR)+i;
            if (week > 0){
                datyWeek.add(week);
            }
            if (week==0){
                datyWeek.add(53);
            }
            if (week<0){
                datyWeek.add(53+week);
            }

        }
        model.addAttribute("dtW", datyWeek);

        // tygodnie sprzedazy Bega wstecz
        List<Integer> soldBegaWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldBegaWeeklyList.add(
                    orderService.getBegaWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyBega",soldBegaWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldWojcikWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldWojcikWeeklyList.add(
                    orderService.getWojcikWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyWojcik",soldWojcikWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);

        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("range3",orderService.getMonthYear());
        return "charts2";
    }

    @PostMapping("/raporty/spedytorzy")
    public String getOrdersInWeekSped (@ModelAttribute
                                          MonthForm monthForm, Integer person,
                                  Model model) {

        Date dt = new Date();
//////////// tworzenie listy
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

        Calendar cal = Calendar.getInstance();
        List<Integer> datyWeek = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
            int week = cal.get(Calendar.WEEK_OF_YEAR)+i;
            if (week > 0){
                datyWeek.add(week);
            }
            if (week==0){
                datyWeek.add(53);
            }
            if (week<0){
                datyWeek.add(53+week);
            }

        }
        model.addAttribute("dtW", datyWeek);
        // tygodnie sprzedazy Bega wstecz
        List<Integer> soldBegaWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldBegaWeeklyList.add(
                    orderService.getBegaWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyBega",soldBegaWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldWojcikWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldWojcikWeeklyList.add(
                    orderService.getWojcikWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyWojcik",soldWojcikWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);

        model.addAttribute("sold", lst);
        model.addAttribute("dt", datyRok);

        model.addAttribute("orders", orderService.getMonthRaportByPerson(monthForm.getLoadDate(), person));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("range3",orderService.getMonthYear());

        return "charts2";
    }

    @GetMapping("/raporty/kierowcy")
    public String getChartsMonthDrivers (Model model) {
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

        Calendar cal = Calendar.getInstance();
        List<Integer> datyWeek = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
            int week = cal.get(Calendar.WEEK_OF_YEAR)+i;
            if (week > 0){
                datyWeek.add(week);
            }
            if (week==0){
                datyWeek.add(53);
            }
            if (week<0){
                datyWeek.add(53+week);
            }

        }
        model.addAttribute("dtW", datyWeek);

        // tygodnie sprzedazy Bega wstecz
        List<Integer> soldBegaWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldBegaWeeklyList.add(
                    orderService.getBegaWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyBega",soldBegaWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldWojcikWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldWojcikWeeklyList.add(
                    orderService.getWojcikWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyWojcik",soldWojcikWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);

        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("range3",orderService.getMonthYear());
        return "charts3";
    }

    @PostMapping("/raporty/kierowcy")
    public String getOrdersMonthDrivers (@ModelAttribute
                                               MonthForm monthForm, Integer person,
                                       Model model) {

        Date dt = new Date();
//////////// tworzenie listy
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

        Calendar cal = Calendar.getInstance();
        List<Integer> datyWeek = new LinkedList<>();
        for (int i = 0; i > -12; i--) {
            int week = cal.get(Calendar.WEEK_OF_YEAR)+i;
            if (week > 0){
                datyWeek.add(week);
            }
            if (week==0){
                datyWeek.add(53);
            }
            if (week<0){
                datyWeek.add(53+week);
            }

        }
        model.addAttribute("dtW", datyWeek);
        // tygodnie sprzedazy Bega wstecz
        List<Integer> soldBegaWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldBegaWeeklyList.add(
                    orderService.getBegaWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyBega",soldBegaWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldWojcikWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldWojcikWeeklyList.add(
                    orderService.getWojcikWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyWojcik",soldWojcikWeeklyList);

        // tygodnie sprzedazy Wojcik wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);

        model.addAttribute("sold", lst);
        model.addAttribute("dt", datyRok);


        if (person > 0) {
            model.addAttribute("orders", orderService.getMonthRaportByDriver(monthForm.getLoadDate(), person));
        }else{
            model.addAttribute("orders", orderService.getMonthRaportByallDrivers(monthForm.getLoadDate()));
        }
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("range3",orderService.getMonthYear());

        return "charts3";
    }

}
