package pl.lukabrasi.transportapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.model.City;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.model.User;
import pl.lukabrasi.transportapp.repository.OrderRepository;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public List<Order> getOrders() {

        return orderRepository.findAll();
    }



    public Optional<Order> getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder;
    }

    public void saveOrder(OrderForm orderForm) {

        Order orderNew = new Order();
        orderNew.setOrderNumber(orderForm.getOrderNumber());


        orderNew.setLoadDate(orderForm.getLoadDate());
        orderNew.setPrice(orderForm.getPrice());
        orderNew.setFreighterPrice(orderForm.getFreighterPrice());



        orderRepository.save(orderNew);
    }


    public void updateOrder(Long id, OrderForm orderForm) {


        Optional<Order> optionalOrder = orderRepository.findById(id);

        optionalOrder.get().setPrice(orderForm.getPrice());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());
        //  optionalWeatherLogEntity.get().setQueryTime(weatherForm.getQueryTime());
        orderRepository.save(optionalOrder.get());


    }


}
