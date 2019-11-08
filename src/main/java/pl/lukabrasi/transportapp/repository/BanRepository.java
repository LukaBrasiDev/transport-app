package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Ban;
import pl.lukabrasi.transportapp.model.User;

import java.util.List;

public interface BanRepository extends JpaRepository<Ban, Long> {

    List<Ban> findAllByOrderByStatusDescFreighterAsc();
}
