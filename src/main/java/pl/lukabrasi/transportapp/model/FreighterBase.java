package pl.lukabrasi.transportapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "freighter_base")
public class FreighterBase {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private @Column(name = "name")
    String name;
    private @Column(name = "address")
    String address;
    private @Column(name = "person")
    String person;
    private @Column(name = "telephone")
    String telephone;
    private @Column(name = "info")
    String info;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_user")
    private User user;
    private @Column(name = "query_time")
    LocalDateTime queryTime;

}
