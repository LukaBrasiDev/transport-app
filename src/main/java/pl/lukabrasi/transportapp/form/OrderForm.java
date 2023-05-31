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
    private String comment;
    private Integer kilometers;
    private String priceExpected;
    private BigDecimal price;
    private Boolean priceConfirmed;
    private BigDecimal freighterPrice;
    private String freighter;
    private User user;
    private OurDriver driver;
    //import
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docDateExp;
    private String exportEnd;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docDateImp;
    private BigDecimal cabotage;
    private String loadingCityImp;
    private String nextLoadingCityImp;
    private String cityCodesImp;
    private Integer kilometersImp;
    private String userImp;
    private BigDecimal priceImp;

}
