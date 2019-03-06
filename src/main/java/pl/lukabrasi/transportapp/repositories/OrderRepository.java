package pl.lukabrasi.transportapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lukabrasi.transportapp.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    
}
