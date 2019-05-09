package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Freighter;

import java.util.List;

public interface FreighterRepository extends JpaRepository<Freighter, Long> {

    List<Freighter> findAllByOrderByFreighterNameAsc();
}