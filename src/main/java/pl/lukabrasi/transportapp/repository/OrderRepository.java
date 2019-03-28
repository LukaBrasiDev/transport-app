package pl.lukabrasi.transportapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {


 Page<Order> findAllByOrderByLoadDateDesc (Pageable pageable);

 Page<Order> findByLoadDateBetweenOrderByLoadDateDesc(LocalDate date1,LocalDate date2,Pageable pageable);

// List<Order> findAllByOrderNumberContains(String number);
}
