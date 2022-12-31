package pl.lukabrasi.transportapp.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class RangeForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date2;
    private String radioSelect;
    private String weekSelect;
    private String checkSelectM;
    private String checkSelectK;
    private String checkSelectI;




}
