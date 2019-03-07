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
@Table(name = "freighter")
public class Freighter {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "freighter_name")
    String freighterName;
    private @Column(name = "freighter_email")
    String freighterEmail;
    private @Column(name = "freighter_info")
    String freighterInfo;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "freighter",
            cascade = {
                    CascadeType.DETACH, // odłączanie kolekcji
                    CascadeType.MERGE,  // aktualizacja encji
                    CascadeType.PERSIST,// włączanie nowej encji do kontekstu
                    CascadeType.REFRESH // odświeżanie stanu encji
                    // CascadeType.REMOVE // usuwanie encji - nie dodajemy, gdyż nie chcemy usuwać kontaktów
                    // w przypadku usunięcia kategorii.
            }
    )
    private Set<Order> orders = new HashSet<>();

    public Freighter(String freighter) {
        this.freighterName = freighter;
    }
}
