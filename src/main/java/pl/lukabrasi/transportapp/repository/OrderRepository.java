package pl.lukabrasi.transportapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findAllByOrderByLoadDateDescLoadingCityAsc(Pageable pageable);

    Page<Order> findByLoadDateBetweenOrderByLoadDateAscLoadingCityAsc(LocalDate date1, LocalDate date2, Pageable pageable);

    @Query(value = "select * from orders where (date_load between ?1 and ?2) and (fk_user is null ) order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findNotSoldOrdersInRange(LocalDate date1, LocalDate date2, Pageable pageable);

    @Query(value = "SELECT * FROM orders where (YEARWEEK(date_load)<=YEARWEEK(NOW())) and (fk_user is null)\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findCurrentWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders where YEARWEEK(date_load)=YEARWEEK(NOW())\n" +
            "UNION  select * from orders where (YEARWEEK(date_load)<YEARWEEK(NOW())) and fk_user is null\n" +
            "           \n" +
            "                   order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findCurrentWeekAll(Pageable pageable);


    boolean existsByOrderNumber(String orderNumber);

    @Query(value = "SELECT count(id) as MTW from orders where month(date_load) = month(now()) and fk_freighter =1", nativeQuery = true)
    int soldByMtwCurrentMonth ();



// List<Order> findAllByOrderNumberContains(String number);
}
