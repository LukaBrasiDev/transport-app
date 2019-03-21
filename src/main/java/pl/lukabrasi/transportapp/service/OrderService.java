package pl.lukabrasi.transportapp.service;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.form.FactoryForm;
import pl.lukabrasi.transportapp.form.OrderForm;
import pl.lukabrasi.transportapp.model.*;
import pl.lukabrasi.transportapp.repository.*;

import java.util.*;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final UserRepository userRepository;
    final FreighterRepository freighterRepository;
    final CodeRepository codeRepository;
    final FactoryRepository factoryRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, FreighterRepository freighterRepository, CodeRepository codeRepository, FactoryRepository factoryRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.freighterRepository = freighterRepository;
        this.codeRepository = codeRepository;
        this.factoryRepository = factoryRepository;
    }

    public List<Order> getOrders() {

        return orderRepository.findAllByOrderByLoadDateDesc();
    }

    public List<Factory> getFactories() {
        return factoryRepository.findAll();
    }

    public List<Code> getCodes() {
        return codeRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Freighter> getFreighters() {
        return freighterRepository.findAll();
    }

    public List<Code> getCities() {
        return codeRepository.findAll();
    }


    public Order getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        }
        return null;
    }

    public Factory getFactoryById(Long id) {
        Optional<Factory> optionalFactory = factoryRepository.findById(id);
        if (optionalFactory.isPresent()) {
            return optionalFactory.get();
        }
        return null;
    }

    public void saveOrder(OrderForm orderForm) {

        Order orderNew = new Order();
        orderNew.setOrderNumber(orderForm.getOrderNumber());
        orderNew.setLoadDate(orderForm.getLoadDate());
        // proba ifa do zaladunku
        String orderPrefix = orderForm.getOrderNumber().toString().substring(0, 3);
        Optional<Factory> cityPrefix = factoryRepository.findFactoryByPrefixContains(orderPrefix);
        if (cityPrefix != null) {
            orderNew.setFactory(cityPrefix.get());
        }
        //  orderNew.setCities(orderForm.getFactoryList());//todo

        // zapisywanie kodow
        Set<Code> codes = new HashSet<Code>();
        String[] stringCodes = orderForm.getCode().split(",");//todo
        for (int i = 0; i < stringCodes.length; i++) {
            Code code = new Code();
            code.setCityCode(stringCodes[i]);
            codes.add(code);
        }

        orderNew.setCodes(codes);
        // kody


        orderNew.setPrice(orderForm.getPrice());
        orderNew.setFreighterPrice(orderForm.getFreighterPrice());
        orderNew.setFreighter(orderForm.getFreighter());
        orderNew.setUser(orderForm.getUser());

        orderRepository.save(orderNew);
    }

    public void saveFactory(FactoryForm factoryForm) {

        Factory factoryNew = new Factory();

        factoryNew.setPrefix(factoryForm.getPrefix());
        factoryNew.setFactoryName(factoryForm.getFactoryName());
        factoryNew.setFactoryCity(factoryForm.getFactoryCity().toUpperCase());
        factoryNew.setFactoryAddress(factoryForm.getFactoryAddress());
        factoryNew.setFactoryContact(factoryForm.getFactoryContact());
        factoryNew.setFactoryInfo(factoryForm.getFactoryInfo());

        factoryRepository.save(factoryNew);
    }


    public void updateOrder(Long id, OrderForm orderForm) {

        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.get().setOrderNumber(orderForm.getOrderNumber());
        optionalOrder.get().setLoadDate(orderForm.getLoadDate());
        // optionalOrder.get().setFactory(orderForm.getFactory());
        //todo

        if (orderForm.getCode()!=null) {
            Set<Code> codes = new HashSet<Code>();
            String[] stringCodes = orderForm.getCode().split(",");//todo
            for (int i = 0; i < stringCodes.length; i++) {
                Code code = new Code();
                code.setCityCode(stringCodes[i]);
                codes.add(code);
            }
           optionalOrder.get().setCodes(codes);
        }



        optionalOrder.get().setPrice(orderForm.getPrice());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());
        optionalOrder.get().setFreighter(orderForm.getFreighter());
        optionalOrder.get().setUser(orderForm.getUser());
        orderRepository.save(optionalOrder.get());//todo if not null

    }

    public void updateFactory(Long id, FactoryForm factoryForm) {

        Optional<Factory> optionalFactory = factoryRepository.findById(id);
        optionalFactory.get().setPrefix(factoryForm.getPrefix());
        optionalFactory.get().setFactoryName(factoryForm.getFactoryName());
        optionalFactory.get().setFactoryCity(factoryForm.getFactoryCity());
        optionalFactory.get().setFactoryAddress(factoryForm.getFactoryAddress());
        optionalFactory.get().setFactoryContact(factoryForm.getFactoryContact());
        optionalFactory.get().setFactoryInfo(factoryForm.getFactoryInfo());

        factoryRepository.save(optionalFactory.get());//todo if not null


    }
   /* public List<Order> getOrdersBySearchValue(String searchString) {

        return orderRepository.findAll(Sor);
    }*/


}
