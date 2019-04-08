package pl.lukabrasi.transportapp.calculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.calculator.model.Route;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route,Long> {

    public boolean findRouteByCityContains(String city);

    public List<Route> findAllByCityContaining(String city);
}
