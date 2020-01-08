package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.OurDriver;
import pl.lukabrasi.transportapp.model.User;

import java.util.List;

public interface OurDriverRepository extends JpaRepository<OurDriver, Long> {


    List<OurDriver> findAllByOrderByDriverSurnameAsc();
}
