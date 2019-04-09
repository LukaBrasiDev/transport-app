package pl.lukabrasi.transportapp.calculator.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class RouteDto {
    private int cityCount;
    private String cityCodesReplaced;
    private String lastCity;
    private BigDecimal finalPrice;
}
