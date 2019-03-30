package pl.lukabrasi.transportapp.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lukabrasi.transportapp.model.Factory;
import pl.lukabrasi.transportapp.model.Freighter;
import pl.lukabrasi.transportapp.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderForm {

    private String orderNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate loadDate;
    private String ourNumber;
    private String loadingCity;
    private Factory factory;
    private String cityCodes;
    private BigDecimal price;
    private BigDecimal freighterPrice;
    private Freighter freighter;
    private User user;


}
