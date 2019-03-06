package pl.lukabrasi.transportapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "city")
public class City {

    private @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "city_name")
    String cityName;
    private @Column(name = "city_code")
    int cityCode;
    private @Column(name = "factory")
    boolean isFactory;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "city")
    private Set<Order> orders = new HashSet<>();
}
