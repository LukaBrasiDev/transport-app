package pl.lukabrasi.transportapp.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lukabrasi.transportapp.model.Factory;
import pl.lukabrasi.transportapp.model.OurDriver;
import pl.lukabrasi.transportapp.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm {

    private String orderNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate loadDate;
    private String ourNumber;
    private String loadHour;
    private String loadingCity;
    private Factory factory;
    private String cityCodes;
    private Integer kilometers;
    private BigDecimal price;
    private BigDecimal freighterPrice;
    private String freighter;
    private User user;
    private OurDriver driver;
    //import
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docDateExp;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docDateImp;
    private String loadingCityImp;
    private String nextLoadingCityImp;
    private String cityCodesImp;
    private Integer kilometersImp;
    private String userImp;
    private BigDecimal priceImp;

}
