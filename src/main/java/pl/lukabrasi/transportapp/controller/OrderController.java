package pl.lukabrasi.transportapp.controller;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.form.MonthForm;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.form.RangeForm;
import pl.lukabrasi.transportapp.form.ReportForm;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.model.User;
import pl.lukabrasi.transportapp.service.OrderService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class OrderController {


    final OrderService orderService;
    final UserSession userSession;

    @Autowired
    public OrderController(UserSession userSession, OrderService orderService) {
        this.userSession = userSession;
        this.orderService = orderService;
    }

    @GetMapping("/zlecenia")
    public String getOrders(Model model,
                            Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
            Page<Order> orderPage = orderService.getOrders(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", "none");
            model.addAttribute("checkSelectM", "inter");
            model.addAttribute("checkSelectK", "country");
            model.addAttribute("checkSelectI", "import");

            return "order";
    }

    @GetMapping("/zlecenia/ceny_niepotwierdzone")
    public String getOrdersCeny (Model model,
                            Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.getNotconfirmedPrices(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("radioSelect", "none");
        model.addAttribute("checkSelectM", "inter");
        model.addAttribute("checkSelectK", "country");
        return "order";
    }

    @GetMapping("/naszeauta")
    public String getOurCars(Model model,
                            Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.findAllMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "import";
    }

    @PostMapping("/zlecenia/tydzien")
    public String getOrdersInWeek(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String selection = rangeForm.getRadioSelect()+rangeForm.getCheckSelectM()+rangeForm.getCheckSelectK()+rangeForm.getCheckSelectI();
        if (selection.equals("allweekintercountryimport") || selection.equals("allweeknullnullnull")) {
            Page<Order> orderPage = orderService.findCurrentWeekAll(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("allweekinternullnull")) {
            Page<Order> orderPage = orderService.findCurrentWeekAllInter(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("allweekinternullimport")) {
            Page<Order> orderPage = orderService.findCurrentWeekAllInterImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("allweeknullcountrynull")) {
            Page<Order> orderPage = orderService.findCurrentWeekAllCountry(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("allweeknullcountryimport")) {
            Page<Order> orderPage = orderService.findCurrentWeekAllCountryImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("allweeknullnullimport")) {
            Page<Order> orderPage = orderService.findCurrentWeekAllImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("nonenullcountrynull")) {
            Page<Order> orderPage = orderService.findCurrentWeekNotSoldCountry(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("nonenullcountryimport")) {
            Page<Order> orderPage = orderService.findCurrentWeekNotSoldCountryImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("noneinternullnull")) {
            Page<Order> orderPage = orderService.findCurrentWeekNotSoldInter(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("noneinternullimport")) {
            Page<Order> orderPage = orderService.findCurrentWeekNotSoldInterImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("nonenullnullimport")) {
            Page<Order> orderPage = orderService.findCurrentWeekNotSoldImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else {
            Page<Order> orderPage = orderService.findCurrentWeekNotSold(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
    }

    @GetMapping("/naszeauta/tydzien")
    public String getOrdersInWeekMTW(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
            Page<Order> orderPage = orderService.findCurrentWeekMTW(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("drivers", orderService.getOurDrivers());
            model.addAttribute("driversFreeWeek", orderService.getDriversFreeWeek());
            model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
            return "import";

    }

    @GetMapping("/naszeauta/tydzienbrak")
    public String getOrdersInWeekMTWempty(@ModelAttribute RangeForm rangeForm,
                                     Model model,
                                     Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.findCurrentWeekMTWempty(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "import";

    }

    @PostMapping("/zlecenia/tydzien/poprzedni")
    public String getOrdersInPreviousWeek(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String selection = rangeForm.getRadioSelect()+rangeForm.getCheckSelectM()+rangeForm.getCheckSelectK()+rangeForm.getCheckSelectI();
        if (selection.equals("allweekintercountryimport") || selection.equals("allweeknullnullnull")) {
             Page<Order> orderPage = orderService.findPreviousWeekAll(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweekinternullnull")) {
            Page<Order> orderPage = orderService.findPreviousWeekAllInter(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweekinternullimport")) {
            Page<Order> orderPage = orderService.findPreviousWeekAllInterImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }


        else if (selection.equals("allweeknullcountrynull")) {
            Page<Order> orderPage = orderService.findPreviousWeekAllCountry(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweeknullcountryimport")) {
            Page<Order> orderPage = orderService.findPreviousWeekAllCountryImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweeknullnullimport")) {
            Page<Order> orderPage = orderService.findPreviousWeekAllImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("noneinternullnull")) {
            Page<Order> orderPage = orderService.findPreviousWeekNotSoldInter(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("noneinternullimport")) {
            Page<Order> orderPage = orderService.findPreviousWeekNotSoldInterImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("nonenullcountrynull")) {
            Page<Order> orderPage = orderService.findPreviousWeekNotSoldCountry(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("nonenullcountryimport")) {
            Page<Order> orderPage = orderService.findPreviousWeekNotSoldCountryImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("nonenullnullimport")) {
            Page<Order> orderPage = orderService.findPreviousWeekNotSoldImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else {
            Page<Order> orderPage = orderService.findPreviousWeekNotSold(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
    }

    @GetMapping("/naszeauta/tydzien/poprzedni")
    public String getOrdersPreviousWeekMTWonly(
                                     Model model,
                                     Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.findPreviousWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("driversFreeWeek", orderService.getDriversFreePreviousWeek());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "import";

    }

    @GetMapping("/naszeauta/tydzien/2poprzednie")
    public String getOrdersTwoPreviousWeekMTW(Model model,
                                           Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.find2PreviousWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "import";

    }

    @GetMapping("/naszeauta/tydzien/3poprzednie")
    public String getOrders3PreviousWeekMTW(Model model,
                                              Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.find3PreviousWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "import";

    }

    @PostMapping("/zlecenia/tydzien/nastepny")
    public String getOrdersInNextWeek(@ModelAttribute RangeForm rangeForm,
                                  Model model,
                                  Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String selection = rangeForm.getRadioSelect()+rangeForm.getCheckSelectM()+rangeForm.getCheckSelectK()+rangeForm.getCheckSelectI();
        if (selection.equals("allweekintercountryimport") || selection.equals("allweeknullnullnull")) {
            Page<Order> orderPage = orderService.findNextWeekAll(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
        else if (selection.equals("allweekinternullnull")) {
            Page<Order> orderPage = orderService.findNextWeekAllInter(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweekinternullimport")) {
            Page<Order> orderPage = orderService.findNextWeekAllInterImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweeknullcountrynull")) {
            Page<Order> orderPage = orderService.findNextWeekAllCountry(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweeknullcountryimport")) {
            Page<Order> orderPage = orderService.findNextWeekAllCountryImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("allweeknullnullimport")) {
            Page<Order> orderPage = orderService.findNextWeekAllImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("noneinternullnull")) {
            Page<Order> orderPage = orderService.findNextWeekNotSoldInter(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("noneinternullimport")) {
            Page<Order> orderPage = orderService.findNextWeekNotSoldInterImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("nonenullcountrynull")) {
            Page<Order> orderPage = orderService.findNextWeekNotSoldCountry(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("nonenullcountryimport")) {
            Page<Order> orderPage = orderService.findNextWeekNotSoldCountryImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else if (selection.equals("nonenullnullimport")) {
            Page<Order> orderPage = orderService.findNextWeekNotSoldImport(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }

        else {
            Page<Order> orderPage = orderService.findNextWeekNotSold(pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("logged", userSession.getUserEntity());
            model.addAttribute("radioSelect", rangeForm.getRadioSelect());
            model.addAttribute("checkSelectM", rangeForm.getCheckSelectM());
            model.addAttribute("checkSelectK", rangeForm.getCheckSelectK());
            model.addAttribute("checkSelectI", rangeForm.getCheckSelectI());
            return "order";
        }
    }

    @GetMapping("/naszeauta/tydzien/nastepny")
    public String getOrdersNextWeekMTW(@ModelAttribute RangeForm rangeForm,
                                     Model model,
                                     Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.findNextWeekMTW(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("driversFreeWeek", orderService.getDriversFreeNextWeek());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "import";

    }
    @PostMapping("/zlecenia/zakres")
    public String findOrdersBetweenDates(@ModelAttribute RangeForm rangeForm,
                                         Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String selection = rangeForm.getRadioSelect();
        if (selection.equals("allorders")) {
           List<Order> orderPage = orderService.getOrdersInRange(rangeForm.getDate1(), rangeForm.getDate2());
            model.addAttribute("orders", orderPage);
            model.addAttribute("totalPages", -1);
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
            model.addAttribute("logged", userSession.getUserEntity());
            return "order";
        } else {
            List<Order> orderPage = orderService.getOrdersInRangeNotSold(rangeForm.getDate1(), rangeForm.getDate2());
            model.addAttribute("orders", orderPage);
            model.addAttribute("totalPages", -1);
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
            model.addAttribute("logged", userSession.getUserEntity());
            return "order";
        }

    }

    @PostMapping("/naszeauta/zakres")
    public String findMTWOrdersBetweenDates(@ModelAttribute RangeForm rangeForm,
                                         Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
            List<Order> orderPage = orderService.findMTWOrdersInRange(rangeForm.getDate1(), rangeForm.getDate2());
            model.addAttribute("orders", orderPage);
            model.addAttribute("totalPages", -1);
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
            model.addAttribute("range1", rangeForm.getDate1());
            model.addAttribute("range2", rangeForm.getDate2());
        model.addAttribute("logged", userSession.getUserEntity());
            return "import";

        }

    @GetMapping("/zlecenia/tura")
    public String findOrderNumber(@RequestParam String searchStr,
                                         Model model,
                                         Pageable pageable) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
            Page<Order> orderPage = orderService.findAllByOrderNumberContains(searchStr, pageable);
            model.addAttribute("page", orderPage);
            model.addAttribute("number", orderPage.getNumber());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalElements", orderPage.getTotalElements());
            model.addAttribute("size", orderPage.getSize());
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("users", orderService.getUsers());
            model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("radioSelect", "none");
          //  model.addAttribute("range1", rangeForm.getDate1());
          //  model.addAttribute("range2", rangeForm.getDate2());
            return "order";


    }


    @PostMapping("/zlecenia")
    public String createOrder(@ModelAttribute OrderForm orderForm,
                              Model model,
                              Pageable pageable) throws UnknownHostException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String loggedUser = userSession.getUserEntity().getUserName();
        OrderService.ActionResponse actionResponse = orderService.saveOrder(orderForm, loggedUser);
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
            model.addAttribute("logged", userSession.getUserEntity());
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
        model.addAttribute("logged", userSession.getUserEntity());
        return "order";
    }

    @PostMapping("/zleceniaimport")
    public String createOrderImport(@ModelAttribute OrderForm orderForm,
                              Model model,
                              Pageable pageable) throws UnknownHostException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String loggedUser = userSession.getUserEntity().getUserName();
        OrderService.ActionResponse actionResponse = orderService.saveOrderImport(orderForm, loggedUser);
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
            model.addAttribute("logged", userSession.getUserEntity());
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
        model.addAttribute("logged", userSession.getUserEntity());
        return "order";
    }

    @PostMapping("/zlecenia/ceny_niepotwierdzone")
    public String createOrder(Model model,
                              Pageable pageable) throws UnknownHostException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Page<Order> orderPage = orderService.getNotconfirmedPrices(pageable);
        model.addAttribute("page", orderPage);
        model.addAttribute("number", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("size", orderPage.getSize());
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("logged", userSession.getUserEntity());
        return "order";
    }


    @GetMapping("/edycjazlecenie/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
       model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("freightersasc", orderService.getFreightersSorted());
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("logged", userSession.getUserEntity());
        return "edit";
    }



    @PostMapping("/edycjazlecenie/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm, Model model) throws UnknownHostException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String loggedUser = userSession.getUserEntity().getUserName();
        OrderService.ActionResponse actionResponse = orderService.updateOrder(id, orderForm, loggedUser);
        model.addAttribute("logged", userSession.getUserEntity());
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
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("freightersasc", orderService.getFreightersSorted());
        model.addAttribute("factories", orderService.getFactories());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("logged", userSession.getUserEntity());
        return "editimport";
    }

    @PostMapping("/edycjaimport/{id}")
    public String updateImport(
            @PathVariable Long id,
            @ModelAttribute OrderForm orderForm, Model model) throws UnknownHostException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        String loggedUser = userSession.getUserEntity().getUserName();
        OrderService.ActionResponse actionResponse = orderService.updateOrderImport(id, orderForm, loggedUser);
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("info", actionResponse);
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("freighters", orderService.getFreighters());
        model.addAttribute("drivers", orderService.getOurDrivers());
        return "editimport";
    }

    @GetMapping("/raporty/wykresy")
    public String getCharts(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        Date dt = new Date();

        List<List<Integer>> soldList = new LinkedList<>();
        for (int i = 0; i > -24; i--) {
            soldList.add(orderService.soldByMtwInCurrentMonth(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusMonths(i)));
        }
        List<Object> lst = soldList.stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        List<LocalDate> datyRok = new LinkedList<>();
        for (int i = 0; i > -24; i--) {
            datyRok.add(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusMonths(i));
        }
        model.addAttribute("sold", lst);
        model.addAttribute("dt", datyRok);

        Calendar cal = Calendar.getInstance();
        List<Integer> datyWeek = new LinkedList<>();
        for (int i = 0; i > -24; i--) {
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

        // tygodnie sprzedazy Mondi wstecz
        List<Integer> soldMondiWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldMondiWeeklyList.add(
                    orderService.getMondiWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyMondi",soldMondiWeeklyList);

        // tygodnie sprzedazy Kraj wstecz
        List<Integer> soldCountryWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldCountryWeeklyList.add(
                    orderService.getCountryWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyCountry",soldCountryWeeklyList);

        // tygodnie sprzedazy Inne wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());

        return "charts";
    }

    @GetMapping("/raporty/spedytorzy")
    public String getChartsSpedytors (   ReportForm reportForm,Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("userForm", reportForm);
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());
        return "charts2";
    }

    @PostMapping("/raporty/spedytorzy")
    public String getOrdersInWeekSped (@ModelAttribute
                                               ReportForm reportForm,
                                  Model model, HttpServletResponse response) throws IOException, JRException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        if (reportForm.getReportFormat().equalsIgnoreCase("PDF")) {
            orderService.getMonthRaportByPerson( LocalDate.parse(reportForm.getLoadDate()+"-01",  DateTimeFormatter.ofPattern("yyyy-MM-dd")), reportForm.getPerson(), reportForm.getReportFormat());
        }

        if (reportForm.getReportFormat().equalsIgnoreCase("html")) {
            response.setContentType("text/html");
            JasperPrint jasperPrint = null;
            jasperPrint = orderService.getMonthRaportByPerson(LocalDate.parse(reportForm.getLoadDate()+"-01",  DateTimeFormatter.ofPattern("yyyy-MM-dd")), reportForm.getPerson(), reportForm.getReportFormat());
            HtmlExporter htmlExporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
            htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
            htmlExporter.exportReport();

        }
        model.addAttribute("selectedUser", reportForm.getPerson());
        model.addAttribute("selectedLoadDate", reportForm.getLoadDate());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());

        return "charts2";
    }


    @GetMapping("/raporty/kierowcy")
    public String getChartsMonthDrivers (Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
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

        // tygodnie sprzedazy Mondi wstecz
        List<Integer> soldMondiWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldMondiWeeklyList.add(
                    orderService.getMondiWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyMondi",soldMondiWeeklyList);

        // tygodnie sprzedazy Inne wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("range3",orderService.getMonthYear());
        return "charts3";
    }

    @PostMapping("/raporty/kierowcy")
    public String getOrdersMonthDrivers (@ModelAttribute
                                               RangeForm rangeForm, Integer person,
                                       Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
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

        // tygodnie sprzedazy Mondi wstecz
        List<Integer> soldMondiWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldMondiWeeklyList.add(
                    orderService.getMondiWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyMondi",soldMondiWeeklyList);

        // tygodnie sprzedazy Inne wstecz
        List<Integer> soldOtherWeeklyList = new LinkedList<>();
        for (int i=0; i>-12;i--){
            soldOtherWeeklyList.add(
                    orderService.getOtherWeekly(LocalDate.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusWeeks(i)));
        }
        model.addAttribute("weeklyOther",soldOtherWeeklyList);

        model.addAttribute("sold", lst);
        model.addAttribute("dt", datyRok);


        if (person > 0) {
            model.addAttribute("orders", orderService.getMonthRaportByDriver(rangeForm.getDate1(),rangeForm.getDate2(), person));
        }else{
            model.addAttribute("orders", orderService.getMonthRaportByallDrivers(rangeForm.getDate1(),rangeForm.getDate2()));
        }
        model.addAttribute("drivers", orderService.getOurDrivers());
        model.addAttribute("range3",orderService.getMonthYear());
        model.addAttribute("logged", userSession.getUserEntity());

        return "charts3";
    }

    @PostMapping("/oferta")
    public String getOfert (@ModelAttribute
                                               OrderForm orderForm,
                                       Model model, HttpServletResponse response) throws IOException, JRException {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        orderService.getOfert();
        model.addAttribute("kilometers", orderForm.getKilometers());
        model.addAttribute("selectedLoadDate", orderForm.getLoadDate());
        model.addAttribute("users", orderService.getUsers());
        model.addAttribute("logged", userSession.getUserEntity());

        return "order";
    }

}
