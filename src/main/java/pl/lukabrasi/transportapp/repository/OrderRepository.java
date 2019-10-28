package pl.lukabrasi.transportapp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderNumberContainsOrderByLoadDateAscLoadingCityAsc(String searchStr, Pageable pageable);

    Page<Order> findAllByOrderByLoadDateDescLoadingCityAsc(Pageable pageable);

    Page<Order> findAllByOrderByIdDesc(Pageable pageable);

    Page<Order> findByLoadDateBetweenOrderByLoadDateAscLoadingCityAsc(LocalDate date1, LocalDate date2, Pageable pageable);

    @Query(value = "select * from orders where (date_load between ?1 and ?2) and (fk_user is null ) order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findNotSoldOrdersInRange(LocalDate date1, LocalDate date2, Pageable pageable);

    @Query(value = "SELECT * FROM orders where year(date_load)=year(now()) and (YEARWEEK(date_load)<=YEARWEEK(NOW())) and (fk_user is null)\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findCurrentWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders where year(date_load)=year(now()) and (YEARWEEK(date_load)=(YEARWEEK(NOW())-1)) and (fk_user is null)\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findPreviousWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders where year(date_load)=year(now()) and year(date_load)=year(?1) and (YEARWEEK(date_load)=(YEARWEEK(NOW())+1)) and (fk_user is null)\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findNextWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders where year(date_load)=year(now()) and YEARWEEK(date_load)=YEARWEEK(NOW())\n" +
            "UNION  select * from orders where year(date_load)=year(now()) and (YEARWEEK(date_load)<YEARWEEK(NOW())) and fk_user is null\n" +
            "           \n" +
            "                   order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findCurrentWeekAll(Pageable pageable);

    @Query(value = "SELECT * FROM orders where year(date_load)=year(now()) and YEARWEEK(date_load)=(YEARWEEK(NOW())-1)\n" +
            "UNION  select * from orders where year(date_load)=year(now()) and (YEARWEEK(date_load)=YEARWEEK(NOW())-1) and fk_user is null\n" +
            "           \n" +
            "                   order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findPreviousWeekAll(Pageable pageable);

    @Query(value = "SELECT * FROM orders where year(date_load)=year(now()) and YEARWEEK(date_load)=(YEARWEEK(NOW())+1)\n" +
            "UNION  select * from orders where year(date_load)=year(now()) and (YEARWEEK(date_load)=YEARWEEK(NOW())+1) and fk_user is null\n" +
            "           \n" +
            "                   order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findNextWeekAll(Pageable pageable);

    boolean existsByOrderNumber(String orderNumber);

    @Query(value = "SELECT count(id) as Sprzedane \n" +
            "from orders \n" +
            "where year(date_load)=year(?1) and month(date_load) = month(?1) and fk_freighter >1\n" +
            "union all\n" +
            "SELECT count(id) as MTW \n" +
            "from orders \n" +
            "where year(date_load)=year(?1) and month(date_load) = month(?1) and fk_freighter =1", nativeQuery = true)
    List<Integer> soldByMtwCurrentMonth (LocalDate date1);

    @Query(value = "select * from orders\n" +
            "where\n" +
            "(month(date_load)  = month(?1)) and (year(date_load) = year(?1)) and fk_user=(?2) order by date_load asc",nativeQuery = true)
    List<Order> monthRaportByPerson (LocalDate loadDate, int person);

@Query(value="SELECT distinct concat(1,'-',month(date_load),'-', year(date_load)) as data FROM orders order by data desc;",nativeQuery = true)
    List<String> getMonthYear();

    //@Query(value = 		" SELECT f.factory_name, count(o.id) as Suma from orders as o, factory as f  where YEARWEEK(o.date_load)=YEARWEEK(?1) and  o.fk_factory = f.id group by f.factory_name", nativeQuery = true)
   // Map<String,Integer> soldByFactoryWeekly (LocalDate date1);



    @Query(value="SELECT count(o.id) as Bega from orders as o, factory as f where o.fk_factory = f.id and year(o.date_load)=year(?1) and YEARWEEK(o.date_load)=YEARWEEK(?1) and o.fk_freighter >0 and f.factory_group = 'B'",nativeQuery = true)
    Integer soldBegaGroupWeekly(LocalDate date1);
    @Query(value="SELECT count(o.id) as Wojcik from orders as o, factory as f where o.fk_factory = f.id and year(o.date_load)=year(?1) and YEARWEEK(o.date_load)=YEARWEEK(?1) and o.fk_freighter >0 and f.factory_group = 'W'",nativeQuery = true)
    Integer soldWojcikGroupWeekly(LocalDate date1);
    @Query(value="SELECT count(o.id) as Inne from orders as o, factory as f where o.fk_factory = f.id and year(o.date_load)=year(?1) and YEARWEEK(o.date_load)=YEARWEEK(?1) and o.fk_freighter >0 and f.factory_group = 'I'",nativeQuery = true)
    Integer soldOtherGroupWeekly(LocalDate date1);


}
