package pl.lukabrasi.transportapp.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BanForm {
    private String freighter;
    private String city;
    private String nip;
    private String description;
    private String status;
    private LocalDate queryTime;
    private String ipAddress;
}



