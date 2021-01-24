package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.Freighter;
import pl.lukabrasi.transportapp.model.FreighterBase;

import java.util.List;
import java.util.Optional;

public interface FreighterBaseRepository extends JpaRepository<FreighterBase, Long> {

    List<FreighterBase> findAllByOrderByNameAsc();

    boolean existsByName(String name);

}