package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.OurDriver;
import pl.lukabrasi.transportapp.model.User;

import java.util.List;

public interface OurDriverRepository extends JpaRepository<OurDriver, Long> {

    @Query(value = "select * from driver order by active desc, driver_surname asc", nativeQuery = true)
    List<OurDriver> findAllByOrderByDriverSurnameAscAndActiveDesc();

    boolean existsByDriverNameAndDriverSurname(String driverName, String driverSurname);



}
