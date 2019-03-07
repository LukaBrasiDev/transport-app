package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {




}
