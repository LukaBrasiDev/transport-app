package pl.lukabrasi.transportapp.calculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.calculator.model.Route;

public interface RouteRepository extends JpaRepository<Route,Long> {
}
