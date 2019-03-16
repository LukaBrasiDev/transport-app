package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lukabrasi.transportapp.model.Factory;
import java.util.Optional;

@Repository
public interface FactoryRepository extends JpaRepository<Factory,Long> {

   Optional<Factory> findFactoryByPrefixContains(String prefix);
}
