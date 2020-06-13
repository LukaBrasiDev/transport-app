package pl.lukabrasi.transportapp.form;

import lombok.Data;
import pl.lukabrasi.transportapp.model.User;

@Data
public class OurDriverForm {
    private String driverName;
    private String driverSurname;
    private String driverCar;
    private String driverSemitrailer;
    private User user;
    private Boolean active;
}
