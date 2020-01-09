package pl.lukabrasi.transportapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class OurDriver {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "driver_name")
    String driverName;
    private @Column(name = "driver_surname")
    String driverSurname;
    private @Column(name = "driver_phone")
    String driverPhone;
    private @Column(name = "driver_email")
    String driverEmail;
    private @Column(name = "driver_info")
    String driverInfo;
    private @Column(name = "active")
    Boolean active;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver",
            cascade = {
                    CascadeType.DETACH, // odłączanie kolekcji
                    CascadeType.MERGE,  // aktualizacja encji
                    CascadeType.PERSIST,// włączanie nowej encji do kontekstu
                    CascadeType.REFRESH // odświeżanie stanu encji
                    // CascadeType.REMOVE // usuwanie encji - nie dodajemy, gdyż nie chcemy usuwać zleceń
                    // w przypadku usunięcia przewoźnika.
            }
    )
    private Set<Order> orders = new HashSet<>();

    public OurDriver(String driver) {
        this.driverSurname = driver;
    }
}
