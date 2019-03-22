package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {


 List<Order> findAllByOrderByLoadDateDesc ();

 List<Order> findAllByOrderNumberContains(String number);
}
