package pl.lukabrasi.transportapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Driver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private @Column(name = "date_load")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate loadDate;

    private @Column(name = "order_number")
    String orderNumber;

    private @Column(name = "load_hour")
    String loadHour;

    private @Column(name = "our_number")
    String ourNumber;

    private BigDecimal price;

    private @Column(name = "freighter_price")
    BigDecimal freighterPrice;

    private @Column(name = "loading_city")
    String loadingCity;

    @JoinColumn(name = "city_codes")
    private String cityCodes;

    private @Column(name = "query_time")
    LocalDateTime queryTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_factory")
    private Factory factory;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_user")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_freighter")
    private Freighter freighter;


}
