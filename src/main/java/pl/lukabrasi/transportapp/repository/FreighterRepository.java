package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Freighter;

import java.util.List;
import java.util.Optional;

public interface FreighterRepository extends JpaRepository<Freighter, Long> {

    List<Freighter> findAllByOrderByFreighterNameAsc();

    Optional<Freighter> findFreighterByFreighterName(String freighterName);
    Freighter findByFreighterName (String freighterName);
}