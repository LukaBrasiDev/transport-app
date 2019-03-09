package pl.lukabrasi.transportapp.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lukabrasi.transportapp.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class OrderForm {

   private Long orderNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
   private LocalDate loadDate;
   private BigDecimal price;
   private BigDecimal freighterPrice;
   private User user;


}
