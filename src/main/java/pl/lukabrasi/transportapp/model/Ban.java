package pl.lukabrasi.transportapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bans")
public class Ban {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String freighter;
    private String city;
    private String nip;
    private String description;
    private String status;
    private @Column(name = "query_time")
    LocalDateTime queryTime;
    private @Column(name = "ipaddress")
    String ipAddress;

}
