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
@Table(name = "factory")
public class Factory {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private @Column(name = "prefix")
    String prefix;

    private @Column(name = "factory_name")
    String factoryName;

    private @Column(name = "city")
    String factoryCity;

    private @Column(name = "factory_address")
    String factoryAddress;

    private @Column(name = "factory_contact")
    String factoryContact;

    private @Column(name = "factory_info")
    String factoryInfo;

    private @Column(name = "factory_group")
    String factoryGroup;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "factory",
            cascade = {
                    CascadeType.DETACH, // odłączanie kolekcji
                    CascadeType.MERGE,  // aktualizacja encji
                    CascadeType.PERSIST,// włączanie nowej encji do kontekstu
                    CascadeType.REFRESH // odświeżanie stanu encji
                    // CascadeType.REMOVE // usuwanie encji - nie dodajemy, gdyż nie chcemy usuwać zleceń
                    // w przypadku usunięcia fabryki.
            }
    )
    private Set<Order> orders = new HashSet<>();

    public Factory(String factory) {
        this.factoryCity = factory;
    }


}
