package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
}
