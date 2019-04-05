package pl.lukabrasi.transportapp.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.calculator.model.Route;
import pl.lukabrasi.transportapp.calculator.repository.RouteRepository;

import java.util.List;

@Service
public class RouteService {

    final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getRoutes(){
        return routeRepository.findAll();
    }
}
