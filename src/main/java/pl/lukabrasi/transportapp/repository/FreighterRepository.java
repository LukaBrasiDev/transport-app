package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.Freighter;

import java.util.List;
import java.util.Optional;

public interface FreighterRepository extends JpaRepository<Freighter, Long> {

    List<Freighter> findAllByOrderByFreighterNameAsc();


    @Query(value = "select * from freighter where UPPER(freighter_name) = UPPER(?1)", nativeQuery = true)
    Optional<Freighter> findFreighterByFreighterName(String freighterName);


    Freighter findByFreighterName (String freighterName);


}