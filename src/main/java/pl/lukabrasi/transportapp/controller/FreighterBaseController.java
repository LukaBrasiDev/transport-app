package pl.lukabrasi.transportapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lukabrasi.transportapp.auth.services.UserSession;
import pl.lukabrasi.transportapp.form.BanForm;
import pl.lukabrasi.transportapp.form.FreighterBaseForm;
import pl.lukabrasi.transportapp.model.FreighterBase;
import pl.lukabrasi.transportapp.service.OrderService;

@Controller
public class FreighterBaseController {

    final OrderService orderService;
    final UserSession userSession;

    @Autowired
    public FreighterBaseController(UserSession userSession, OrderService orderService) {
        this.userSession = userSession;
        this.orderService = orderService;
    }

    @GetMapping("/bazaprzewoznikow")
    public String getBlocked(Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("freighterbase", orderService.getFreighterBaseSorted());
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("users", orderService.getUsers());
        return "freighterbase";
    }

    @PostMapping("/bazaprzewoznikow")
    public String createFreighter(@ModelAttribute FreighterBaseForm freighterBaseForm, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        OrderService.ActionResponse actionResponse = orderService.saveFreighterBase(freighterBaseForm);
       /* if (actionResponse == OrderService.ActionResponse.ZAKAZOK) {
        orderService.saveBan(banForm);
        model.addAttribute("ban", orderService.getBansSorted());
            model.addAttribute("info", actionResponse);
        return "ban";}*/
        model.addAttribute("freighterbase", orderService.getFreighterBaseSorted());
        model.addAttribute("info", actionResponse);
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("users", orderService.getUsers());
        return "freighterbase";
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

    @GetMapping("/bazaprzewoznikow/{id}")
    public String register(
            @PathVariable Long id,
            @ModelAttribute FreighterBaseForm freighterBaseForm,
            Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("freighterbase", orderService.getFreighterBaseById(id));
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("users", orderService.getUsers());
        orderService.updateFreighterBase(id, freighterBaseForm);

        return "editfreighterbase";

    }

    @RequestMapping(value = "/bazaprzewoznikow/usun/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable(value = "id") Long id) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }

        orderService.deleteFreighterBaseById(id);
        return "redirect:/bazaprzewoznikow";
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

    @GetMapping("/bazaprzewoznikowedycja/{id}")
    public String edit(@PathVariable Long id, Model model) {
        if (!userSession.isLogin()) {
            return "redirect:/";
        }
        model.addAttribute("freighterbase", orderService.getFreighterBaseById(id));
        model.addAttribute("freighterBaseForm", new FreighterBaseForm());
        model.addAttribute("logged", userSession.getUserEntity());
        model.addAttribute("users", orderService.getUsers());
        return "editfreighterbase";
    }

@PostMapping("/bazaprzewoznikowedycja/{id}")
public String updateFreighter(
        @PathVariable Long id,
        @ModelAttribute FreighterBaseForm freighterBaseForm) {
    if (!userSession.isLogin()) {
        return "redirect:/";
    }
    orderService.updateFreighterBase(id, freighterBaseForm);
    return "redirect:/bazaprzewoznikow";
}

}
