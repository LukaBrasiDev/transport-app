package pl.lukabrasi.transportapp.form;

import lombok.Data;

@Data
public class OurDriverForm {
    private String driverName;
    private String driverSurname;
    private String driverCar;
    private String driverSemitrailer;
    private Boolean active;
}
