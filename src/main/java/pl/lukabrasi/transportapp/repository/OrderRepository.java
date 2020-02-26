package pl.lukabrasi.transportapp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderNumberContainsOrderByLoadDateAscLoadingCityAsc(String searchStr, Pageable pageable);

    Page<Order> findAllByOrderByLoadDateDescLoadingCityAsc(Pageable pageable);

    Page<Order> findAllByOrderByIdDesc(Pageable pageable);

    List<Order> findByLoadDateBetweenOrderByLoadDateAscLoadingCityAsc(LocalDate date1, LocalDate date2);

    @Query(value = "select * from orders where (date_load between ?1 and ?2) and (fk_user is null ) order by date_load asc, loading_city asc", nativeQuery = true)
    List<Order> findNotSoldOrdersInRange(LocalDate date1, LocalDate date2);

    //tututututututu
/*   @Query(value = "SELECT * FROM orders as o INNER JOIN freighter as f\n" +
            "INNER JOIN user as u\n" +
            "where o.fk_freighter = f.id and o.fk_user = u.id\n" +
            "and o.date_load >= CAST(?1 AS DATE) and o.date_load <= CAST(?2 AS DATE)\n" +
             "and f.freighter_name = 'MTW'\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
         List<Order> findMTWOrdersInRange(LocalDate date1, LocalDate date2, Pageable pageable);*/

    //updated
    @Query(value = "SELECT * FROM orders o where fk_freighter = 1 and date_load >= ?1 and date_load <= ?2 order by date_load desc, fk_user asc", nativeQuery = true)
    List<Order> findMTWOrdersInRange(LocalDate date1, LocalDate date2);


    @Query(value = "SELECT * FROM orders where YEARWEEK(date_load)<=YEARWEEK(curdate()) and fk_user is null\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findCurrentWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders where YEARWEEK(date_load)=yearweek(SUBDATE(curdate(), INTERVAL 7 DAY)) and fk_user is null\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findPreviousWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders where YEARWEEK(date_load)=yearweek(DATE_ADD(curdate(), INTERVAL 7 DAY)) and fk_user is null\n" +
            "            order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findNextWeekNotSold(Pageable pageable);

    @Query(value = "SELECT * FROM orders \n" +
            "where YEARWEEK(date_load)=YEARWEEK(curdate())\n" +
            "UNION\n" +
            "select * \n" +
            "from orders \n" +
            "where YEARWEEK(date_load)<YEARWEEK(curdate()) and fk_user is null\n" +
            "order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findCurrentWeekAll(Pageable pageable);

    @Query(value = "SELECT * \n" +
            "FROM orders \n" +
            "where YEARWEEK(date_load)=yearweek(SUBDATE(curdate(), INTERVAL 7 DAY))\n" +
            "UNION\n" +
            "select * \n" +
            "from orders \n" +
            "where YEARWEEK(date_load)=yearweek(SUBDATE(curdate(), INTERVAL 7 DAY)) and fk_user is null\n" +
            "order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findPreviousWeekAll(Pageable pageable);

    @Query(value = "SELECT *\n" +
            "FROM orders\n" +
            "where YEARWEEK(date_load)=yearweek(DATE_ADD(curdate(), INTERVAL 7 DAY))\n" +
            "UNION  select * \n" +
            "from orders \n" +
            "where YEARWEEK(date_load)=yearweek(DATE_ADD(curdate(), INTERVAL 7 DAY)) and fk_user is null\n" +
            "order by date_load asc, loading_city asc", nativeQuery = true)
    Page<Order> findNextWeekAll(Pageable pageable);

    boolean existsByOrderNumber(String orderNumber);

    //updated
    @Query(value = "SELECT count(o.id) as Sprzedane \n" +
            "FROM orders o\n" +
            "INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "where  year(o.date_load)=year(?1) and month(o.date_load) = month(?1) and f.freighter_name <> 'MTW' and o.fk_freighter IS NOT NULL\n" +
            "UNION ALL\n" +
            "SELECT count(o.id) as MTW\n" +
            "FROM orders o\n" +
            "INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "where  year(o.date_load)=year(?1) and month(o.date_load) = month(?1) and f.freighter_name = 'MTW'", nativeQuery = true)
    List<Integer> soldByMtwCurrentMonth(LocalDate date1);


    @Query(value = "select * from orders\n" +
            "where\n" +
            "(month(date_load)  = month(?1)) and (year(date_load) = year(?1)) and fk_user=(?2) order by date_load asc", nativeQuery = true)
    List<Order> monthRaportByPerson(LocalDate loadDate, Integer person);

/*    @Query(value = "select * from orders\n" +
            "where\n" +
            "(month(date_load)  = month(?1)) and (year(date_load) = year(?1)) and fk_driver=(?2) order by date_load asc", nativeQuery = true)
    List<Order> monthRaportByDriver(LocalDate loadDate, Integer person);*/

    @Query(value = "select * from orders\n" +
            "where\n" +
            "date_load >= ?1 and date_load <= ?2 and fk_driver=(?3) order by date_load asc", nativeQuery = true)
    List<Order> monthRaportByDriver(LocalDate loadDate1, LocalDate loadDate2, Integer person);

  /*  @Query(value = "SELECT * FROM orders o where fk_freighter = 1 and date_load >= ?1 and date_load <= ?2 order by date_load desc, fk_user asc", nativeQuery = true)
    List<Order> findMTWOrdersInRange(LocalDate date1, LocalDate date2);*/



    @Query(value = "select * from orders o, driver d\n" +
            "where o.fk_driver = d.id and\n" +
            "o.date_load >= ?1 and o.date_load <= ?2 order by d.driver_surname asc, o.date_load asc", nativeQuery = true)
    List<Order> monthRaportByAllDrivers(LocalDate loadDate1, LocalDate loadDate2);

    @Query(value = "SELECT distinct concat(1,'-',month(date_load),'-', year(date_load)) as data FROM orders order by date_load desc;", nativeQuery = true)
    List<String> getMonthYear();

    //@Query(value = 		" SELECT f.factory_name, count(o.id) as Suma from orders as o, factory as f  where YEARWEEK(o.date_load)=YEARWEEK(?1) and  o.fk_factory = f.id group by f.factory_name", nativeQuery = true)
    // Map<String,Integer> soldByFactoryWeekly (LocalDate date1);


    @Query(value = "SELECT count(o.id) as Bega from orders o INNER JOIN factory f ON o.fk_factory = f.id where YEARWEEK(o.date_load)=YEARWEEK(?1) and o.fk_user >1 and f.factory_group = 'B'", nativeQuery = true)
    Integer soldBegaGroupWeekly(LocalDate date1);

    @Query(value = "SELECT count(o.id) as Wojcik from orders o INNER JOIN factory f ON o.fk_factory = f.id where YEARWEEK(o.date_load)=YEARWEEK(?1) and o.fk_user >1 and f.factory_group = 'W'", nativeQuery = true)
    Integer soldWojcikGroupWeekly(LocalDate date1);

    @Query(value = "SELECT count(o.id) as Inne from orders o INNER JOIN factory f ON o.fk_factory = f.id where YEARWEEK(o.date_load)=YEARWEEK(?1) and o.fk_user >1 and f.factory_group = 'I'", nativeQuery = true)
    Integer soldOtherGroupWeekly(LocalDate date1);

    //updated
    @Query(value = "SELECT * FROM orders o where fk_freighter = 1 order by date_load desc, fk_user asc", nativeQuery = true)
    Page<Order> findAllMTW(Pageable pageable);


    //TBD
    @Query(value = "SELECT * \n" +
            "FROM orders o INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "INNER JOIN user u ON o.fk_user = u.id\n" +
            "where o.date_load between date_sub(curdate(), interval if(dayofweek(curdate())-5 >= 0, dayofweek(curdate())-5, dayofweek(curdate())-5+7) day)\n" +
            "and date_sub(curdate(), interval if(dayofweek(curdate())-5 >= 0, dayofweek(curdate())-5, dayofweek(curdate())-5+7) - 6 day)\n" +
            "and f.freighter_name = 'MTW'\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
    Page<Order> findCurrentWeekMTW(Pageable pageable);

    @Query(value = "SELECT * \n" +
            "FROM orders o INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "INNER JOIN user u ON o.fk_user = u.id\n" +
            "where o.date_load between date_sub(curdate(), interval if(dayofweek(curdate())-5 >= 0, dayofweek(curdate())-5, dayofweek(curdate())-5+7) day)\n" +
            "and date_sub(curdate(), interval if(dayofweek(curdate())-5 >= 0, dayofweek(curdate())-5, dayofweek(curdate())-5+7) - 6 day)\n" +
            "and f.freighter_name = 'MTW' and (o.loading_city_imp is NULL OR o.loading_city_imp = '')\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
    Page<Order> findCurrentWeekMTWempty(Pageable pageable);

    @Query(value = "SELECT * \n" +
            "FROM orders o INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "INNER JOIN user u ON o.fk_user = u.id\n" +
            "where  o.date_load between date_sub((date_sub(curdate(), interval 7 day)), interval if(dayofweek((date_sub(curdate(), interval 7 day)))-5 >= 0, dayofweek((date_sub(curdate(), interval 7 day)))-5, dayofweek((date_sub(curdate(), interval 7 day)))-5+7) day)\n" +
            "and date_sub((date_sub(curdate(), interval 7 day)), interval if(dayofweek((date_sub(curdate(), interval 7 day)))-5 >= 0, dayofweek((date_sub(curdate(), interval 7 day)))-5, dayofweek((date_sub(curdate(), interval 7 day)))-5+7) - 6 day)\n" +
            "and f.freighter_name = 'MTW'\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
    Page<Order> findPreviousWeekMTW(Pageable pageable);


    @Query(value = "SELECT * \n" +
            "FROM orders o INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "INNER JOIN user u ON o.fk_user = u.id\n" +
            "where o.date_load between date_sub((date_sub(curdate(), interval 14 day)), interval if(dayofweek((date_sub(curdate(), interval 14 day)))-5 >= 0, dayofweek((date_sub(curdate(), interval 14 day)))-5, dayofweek((date_sub(curdate(), interval 14 day)))-5+7) day)\n" +
            "and date_sub((date_sub(curdate(), interval 14 day)), interval if(dayofweek((date_sub(curdate(), interval 14 day)))-5 >= 0, dayofweek((date_sub(curdate(), interval 14 day)))-5, dayofweek((date_sub(curdate(), interval 14 day)))-5+7) - 6 day)\n" +
            "and f.freighter_name = 'MTW'\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
    Page<Order> find2PreviousWeekMTW(Pageable pageable);

    @Query(value = "SELECT * \n" +
            "FROM orders o INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "INNER JOIN user u ON o.fk_user = u.id\n" +
            "where o.date_load between date_sub((date_sub(curdate(), interval 21 day)), interval if(dayofweek((date_sub(curdate(), interval 21 day)))-5 >= 0, dayofweek((date_sub(curdate(), interval 21 day)))-5, dayofweek((date_sub(curdate(), interval 21 day)))-5+7) day)\n" +
            "and date_sub((date_sub(curdate(), interval 21 day)), interval if(dayofweek((date_sub(curdate(), interval 21 day)))-5 >= 0, dayofweek((date_sub(curdate(), interval 21 day)))-5, dayofweek((date_sub(curdate(), interval 21 day)))-5+7) - 6 day)\n" +
            "and f.freighter_name = 'MTW'\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
    Page<Order> find3PreviousWeekMTW(Pageable pageable);

    @Query(value = "SELECT * \n" +
            "FROM orders o INNER JOIN freighter f ON o.fk_freighter = f.id\n" +
            "INNER JOIN user u ON o.fk_user = u.id\n" +
            "where o.date_load between date_sub((DATE_ADD(curdate(), INTERVAL 7 DAY)), interval if(dayofweek((DATE_ADD(curdate(), INTERVAL 7 DAY)))-5 >= 0, dayofweek((DATE_ADD(curdate(), INTERVAL 7 DAY)))-5, dayofweek((DATE_ADD(curdate(), INTERVAL 7 DAY)))-5+7) day)\n" +
            "and date_sub((DATE_ADD(curdate(), INTERVAL 7 DAY)), interval if(dayofweek((DATE_ADD(curdate(), INTERVAL 7 DAY)))-5 >= 0, dayofweek((DATE_ADD(curdate(), INTERVAL 7 DAY)))-5, dayofweek((DATE_ADD(curdate(), INTERVAL 7 DAY)))-5+7) - 6 day)\n" +
            "and f.freighter_name = 'MTW'\n" +
            "order by u.user_name asc, o.date_load desc", nativeQuery = true)
    Page<Order> findNextWeekMTW(Pageable pageable);





}
