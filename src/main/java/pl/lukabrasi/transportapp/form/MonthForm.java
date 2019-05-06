package pl.lukabrasi.transportapp.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MonthForm {

    @DateTimeFormat(pattern = "yyyy-M-dd")
    private LocalDate loadDate;


}
