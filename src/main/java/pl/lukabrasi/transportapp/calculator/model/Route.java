package pl.lukabrasi.transportapp.calculator.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "route")
public class Route {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private @Column(name = "postal_code")
    String postalCode;

    private @Column(name = "city")
    String city;

    private @Column(name = "distance")
    int distance;

    private @Column(name = "route_rate")
    BigDecimal routeRate;

    private @Column(name = "country")
    String country;

}
