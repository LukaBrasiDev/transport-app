package pl.lukabrasi.transportapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city")
public class City {

    private @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "city_name")
    String cityName;
    private @Column(name = "city_code")
    int cityCode;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "cities")
    private Set<Order> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return getCityName().equals(city.getCityName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityName());
    }
}
