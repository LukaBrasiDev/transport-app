package pl.lukabrasi.transportapp.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ReportForm {


    private String loadDate;
    private String person;
    private String reportFormat;


}
