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
@Table(name = "user")
public class User {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "user_name")
    String userName;
    private String email;
    private String password;
    private String role;
    private String telephone;
    private Boolean active;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = {
                    CascadeType.DETACH, // odłączanie kolekcji
                    CascadeType.MERGE,  // aktualizacja encji
                    CascadeType.PERSIST,// włączanie nowej encji do kontekstu
                    CascadeType.REFRESH // odświeżanie stanu encji
            }
    )
    private Set<OurDriver> drivers = new HashSet<>();
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = {
                    CascadeType.DETACH, // odłączanie kolekcji
                    CascadeType.MERGE,  // aktualizacja encji
                    CascadeType.PERSIST,// włączanie nowej encji do kontekstu
                    CascadeType.REFRESH // odświeżanie stanu encji
            }
    )
    private Set<Order> orders = new HashSet<>();

    public User(String user) {
        this.userName = user;
    }

}
