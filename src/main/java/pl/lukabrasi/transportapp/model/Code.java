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
@Table(name = "code")
public class Code {

    private @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    private @Column(name = "city_code")
    String cityCode;

    @JsonIgnore
    @ManyToMany(mappedBy = "codes")
    private Set<Order> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Code)) return false;
        Code code = (Code) o;
        return getCityCode().equals(code.getCityCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityCode());
    }
}
