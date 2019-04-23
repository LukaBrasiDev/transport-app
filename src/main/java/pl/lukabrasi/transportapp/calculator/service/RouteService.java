package pl.lukabrasi.transportapp.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.calculator.dto.RouteDto;
import pl.lukabrasi.transportapp.calculator.form.CityForm;
import pl.lukabrasi.transportapp.calculator.form.RouteForm;
import pl.lukabrasi.transportapp.calculator.model.Route;
import pl.lukabrasi.transportapp.calculator.repository.RouteRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RouteService {

    final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }


    public RouteDto calculateRoute(RouteForm routeForm) {
        RouteDto newRoute = new RouteDto();


        List<String> citiesList = new LinkedList<>();
        String[] stringCities = Arrays.asList(routeForm.getRouteStr().split("[/]"))
                .stream()
                .filter(str -> !str.trim().isEmpty())
                .collect(Collectors.toList())
                .toArray(new String[0]);

        for (int i = 0; i < stringCities.length; i++) {
            String trimmed = stringCities[i].trim();
            if (trimmed
                    .contains(" ")) {
                citiesList.add(trimmed
                        .substring(0, trimmed.indexOf(" "))
                        .replaceAll("ü", "u")
                        .replaceAll("ß", "ss")
                        .replaceAll("ö", "o")
                        .replaceAll("ä", "a"));
            } else {
                citiesList.add(trimmed
                        .replaceAll("ü", "u")
                        .replaceAll("ß", "ss")
                        .replaceAll("ö", "o")
                        .replaceAll("ä", "a"));
            }
        }

        newRoute.setCityCount(citiesList.size());

        String lastCity = ((LinkedList<String>) citiesList).getLast();
        newRoute.setLastCity(lastCity);

        List<String> citiesCodes = new LinkedList<>();
        for (String cityName : citiesList) {
            List<Route> routes = new LinkedList<>();

            routes = routeRepository.findAllByCityContaining(cityName);
            if (!routes.isEmpty()) {
                //  routeRepository.findRouteByCityContains(cityName);
                String cityPostal = routes.get(0).getPostalCode();
                citiesCodes.add(cityPostal);
            } else {
                citiesCodes.add(cityName);
            }
        }

        String distanceMax = ((LinkedList<String>) citiesCodes).getLast();

        newRoute.setCityCodesReplaced(citiesCodes.toString()
                .replace(" ", "")
                .replaceAll("\\s{2,}", "")
                .replace("[", "")
                .replace("]", "")
                .replace(",", ", ")
                .trim());

        List<Route> distanceList = new LinkedList<>();
        distanceList = routeRepository.findAllByCityContaining(lastCity);
        if (!distanceList.isEmpty()) {
            int maxDistance = distanceList.stream()
                    .max(Comparator.comparingInt(Route::getDistance))
                    .get().getDistance();
            BigDecimal finalD = BigDecimal.valueOf(maxDistance);
            BigDecimal finaleDistance = finalD.multiply(new BigDecimal(1.08));
            BigDecimal finaler = finaleDistance.setScale(0, BigDecimal.ROUND_HALF_UP);
            BigDecimal calcPrice = BigDecimal.valueOf((citiesList.size() - 1) * 30);
            newRoute.setFinalPrice((calcPrice.add(finaler)));
            return newRoute;
        }
        return newRoute;
    }

    public void saveCity(CityForm cityForm){

        Route routeNew = new Route();
        routeNew.setPostalCode(cityForm.getPostalCode());
        routeNew.setCity(cityForm.getCity());
        routeNew.setDistance(cityForm.getDistance());

        if (!cityForm.getCity().isEmpty() && !cityForm.getPostalCode().isEmpty() && cityForm.getDistance()>0) {
            routeRepository.save(routeNew);
        }
    }

}

