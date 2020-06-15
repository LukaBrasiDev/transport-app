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


    @Query(value = "           SELECT * \n" +
            "           from driver as d\n" +
            "           LEFT JOIN orders as o\n" +
            "         on d.id = o.fk_driver and   \n" +
            "           o.date_load between date_sub(curdate(), interval if(dayofweek(curdate())-5 >= 0, dayofweek(curdate())-5, dayofweek(curdate())-5+7) day)\n" +
            "            and date_sub(curdate(), interval if(dayofweek(curdate())-5 >= 0, dayofweek(curdate())-5, dayofweek(curdate())-5+7) - 6 day)\n" +
            "            left JOIN freighter f ON o.fk_freighter = f.id\n" +
            "            left JOIN user u ON d.fk_user = u.id\n" +
            "            where d.id is true and o.id is null\n" +
            "       order by u.user_name asc, o.date_load desc", nativeQuery = true)
    List<OurDriver> findDriversFreeWeek();



}
