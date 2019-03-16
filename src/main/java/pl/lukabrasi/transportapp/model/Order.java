package pl.lukabrasi.transportapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    Long orderNumber;

    BigDecimal price;

    BigDecimal freighterPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_factory")
    private Factory factory;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_user")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_freighter")
    private Freighter freighter;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinTable(name = "order_code",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "code_id")})
    private Set<Code> codes = new HashSet<>();
   /* public BigDecimal getProfit(BigDecimal price, BigDecimal freighterPrice) {

        if (price.compareTo(BigDecimal.ZERO) > 1 && freighterPrice.compareTo(BigDecimal.ZERO) > 1) {
            BigDecimal profit = price.subtract(freighterPrice);
            return profit;
        }

        return BigDecimal.ONE;
    }*/

/*    public List<Code> getOnlyFactories() {

        List<Code> citiesAreFactory = cities.stream()
                .filter(p -> p.isFactory() == true).collect(Collectors.toList());
        return citiesAreFactory;
    }*/


}
