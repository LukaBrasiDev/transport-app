package pl.lukabrasi.transportapp.calculator.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CityForm {

    private String postalCode;
    private String city;
    private int distance;
    private BigDecimal routeRate;


}
