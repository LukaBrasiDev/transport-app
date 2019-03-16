package pl.lukabrasi.transportapp.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lukabrasi.transportapp.model.Code;
import pl.lukabrasi.transportapp.model.Factory;
import pl.lukabrasi.transportapp.model.Freighter;
import pl.lukabrasi.transportapp.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderForm {

    private Long orderNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate loadDate;
    private Factory factory; //todo lista
    private String cityCode;
    private BigDecimal price;
    private BigDecimal freighterPrice;
    private Freighter freighter;
    private User user;


}
