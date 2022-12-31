package pl.lukabrasi.transportapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


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

    private @Column(name = "price_expected")
    BigDecimal priceExpected;

    private BigDecimal price;

    private @Column(name = "price_confirmed")
    Boolean priceConfirmed;

    private @Column(name = "freighter_price")
    BigDecimal freighterPrice;

    private @Column(name = "loading_city")
    String loadingCity;

    @JoinColumn(name = "city_codes")
    private String cityCodes;

    private String comment;

    private Integer kilometers;

    private @Column(name = "query_time")
    LocalDateTime queryTime;

    private @Column(name = "ipaddress")
    String ipaddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_factory")
    private Factory factory;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_user")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_freighter")
    private Freighter freighter;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_driver")
    private OurDriver driver;

    //is IMPORT or EXPORT
    private @Column(name = "is_import")
    Boolean isImport;

    //IMPORT
    private @Column(name = "cabotage")
    BigDecimal cabotage;

    private @Column(name = "doc_date_exp")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate docDateExp;

    private @Column(name = "doc_date_imp")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate docDateImp;

    private @Column(name = "export_end")
    String exportEnd;

    private @Column(name = "loading_city_imp")
    String loadingCityImp;

    private @Column(name = "city_codes_imp")
    String cityCodesImp;

    private @Column(name = "kilometers_imp")
    Integer kilometersImp;

    private @Column(name = "next_loading_city_imp")
    String nextLoadingCityImp;

    private @Column(name = "price_imp")
    BigDecimal priceImp;

    private @Column(name = "user_imp")
    String userImp;

    private @Column(name = "query_time_imp")
    LocalDateTime queryTimeImp;

    private @Column(name = "ipaddress_imp")
    String ipaddressImp;

    private @Column(name = "turnover")
    BigDecimal turnover;
}
