package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.Ban;
import pl.lukabrasi.transportapp.model.User;

import java.util.List;

public interface BanRepository extends JpaRepository<Ban, Long> {

    @Query(value = "SELECT * FROM bans\n" +
            "WHERE transorsped = true\n" +
            "ORDER BY status desc, freighter asc;", nativeQuery = true)
    List<Ban> findAllByOrderByStatusDescFreighterAsc();

    @Query(value = "SELECT * FROM bans\n" +
            "WHERE transorsped = false\n" +
            "ORDER BY status desc, freighter asc;", nativeQuery = true)
    List<Ban> findAllBanSped();

    boolean existsByFreighter(String freighter);

}
