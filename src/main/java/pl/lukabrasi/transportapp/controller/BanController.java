package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.form.BanForm;
import pl.lukabrasi.transportapp.form.UserForm;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class BanController {

    final OrderService orderService;

    @Autowired
    public BanController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/zakazy")
    public String getBlocked(Model model) {
        model.addAttribute("ban", orderService.getBansSorted());
        return "ban";
    }

    @PostMapping("/zakazy")
    public String createFreighter(@ModelAttribute BanForm banForm, Model model) {
        OrderService.ActionResponse actionResponse = orderService.saveBan(banForm);
       /* if (actionResponse == OrderService.ActionResponse.ZAKAZOK) {
        orderService.saveBan(banForm);
        model.addAttribute("ban", orderService.getBansSorted());
            model.addAttribute("info", actionResponse);
        return "ban";}*/
        model.addAttribute("ban", orderService.getBansSorted());
        model.addAttribute("info", actionResponse);
        return "ban";
    }

/*    @GetMapping("/zakazy/{id}")
    public String getBanById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("ban", orderService.getBanById(id));
        return "ban";
    }*/

/*    @PostMapping("/zakazy/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @ModelAttribute BanForm banForm        ) {
        orderService.updateBanStatus(id, banForm);

        return "redirect:/zakazy";
    }*/

/*    @RequestMapping(value = "/zakazy/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable(value = "id") Long id) {
              orderService.deleteWeatherLogEntityById(id);
        return "redirect:/zakazy";
    }*/

    @GetMapping("/zakazy/{id}")
    public String register(
            @PathVariable Long id,
            @ModelAttribute BanForm banForm,
            Model model) {
        model.addAttribute("ban", orderService.getBanById(id));
        orderService.updateBanStatus(id, banForm);

        return "redirect:/zakazy";

    }

    @RequestMapping(value = "/zakazy/usun/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable(value = "id") Long id) {

        orderService.deleteBanById(id);
        return "redirect:/zakazy";
    }

/*
    @GetMapping("/uzytkownik/{id}")
    public String getUserById(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("users", orderService.getUserById(id));
        return "user";
    }

    @GetMapping("/edycjauzytkownik/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        model.addAttribute("users", orderService.getUserById(id));
        model.addAttribute("userForm", new UserForm());
        return "edituser";
    }

    @PostMapping("/edycjauzytkownik/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute UserForm userForm) {
        orderService.updateUser(id, userForm);
        return "redirect:/uzytkownicy";
    }


    */


}
