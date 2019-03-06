package pl.lukabrasi.transportapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "user")
public class User {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "user_name")
    String userName;
    private String email;
    private String password;
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
