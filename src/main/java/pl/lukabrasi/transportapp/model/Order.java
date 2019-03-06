package pl.lukabrasi.transportapp.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "order")
public class Order {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "date_load")
    LocalDate loadDate;
    private @Column(name = "order_number")
    long orderNumber;
    BigDecimal price;
    BigDecimal freighterPrice;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "order_city",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "city_id") })
    private Set<City> cities = new HashSet<>();


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_user")
    private User user;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_freighter")
    private Freighter freighter;



}
