package pl.lukabrasi.transportapp.form;

import lombok.Data;

@Data
public class OurDriverForm {
    private String driverName;
    private String driverSurname;
    private String driverEmail;
    private String driverPhone;
    private String driverInfo;
    private Boolean driverStatus;
}
