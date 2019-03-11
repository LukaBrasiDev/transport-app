package pl.lukabrasi.transportapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.model.City;
import pl.lukabrasi.transportapp.model.Freighter;
import pl.lukabrasi.transportapp.model.Order;
import pl.lukabrasi.transportapp.model.User;
import pl.lukabrasi.transportapp.repository.CityRepository;
import pl.lukabrasi.transportapp.repository.FreighterRepository;
import pl.lukabrasi.transportapp.repository.OrderRepository;
import pl.lukabrasi.transportapp.repository.UserRepository;

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
    final UserRepository userRepository;
    final FreighterRepository freighterRepository;
    final CityRepository cityRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, FreighterRepository freighterRepository, CityRepository cityRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.freighterRepository = freighterRepository;
        this.cityRepository = cityRepository;
    }

    @Autowired


    public List<Order> getOrders() {

        return orderRepository.findAllByOrderByLoadDateDesc();
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public List<Freighter> getFreighters(){
        return freighterRepository.findAll();
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
        orderNew.setUser(orderForm.getUser());

        orderRepository.save(orderNew);
    }


    public void updateOrder(Long id, OrderForm orderForm) {

        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.get().setOrderNumber(orderForm.getOrderNumber());
        optionalOrder.get().setLoadDate(orderForm.getLoadDate());
        optionalOrder.get().setPrice(orderForm.getPrice());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());
        optionalOrder.get().setFreighter(orderForm.getFreighter());
        optionalOrder.get().setUser(orderForm.getUser());
        orderRepository.save(optionalOrder.get());//todo if not null

    }


   /* public List<Order> getOrdersBySearchValue(String searchString) {

        return orderRepository.findAll(Sor);
    }*/


}
