package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Code;

public interface CodeRepository extends JpaRepository<Code, Long> {
}
