package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Freighter;

public interface FreighterRepository extends JpaRepository<Freighter, Long> {
}
