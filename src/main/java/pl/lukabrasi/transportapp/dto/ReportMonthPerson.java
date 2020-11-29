package pl.lukabrasi.transportapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportMonthPerson {

    private String orderNumber;
    private LocalDate loadDate;
    private String ourNumber;
    private String loadingCity;
    private String cityCodes;
    private BigDecimal price;
    private BigDecimal freighterPrice;
    private String freighterName;


}
