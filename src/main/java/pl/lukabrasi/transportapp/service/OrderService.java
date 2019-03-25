package pl.lukabrasi.transportapp.service;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.form.FactoryForm;
import pl.lukabrasi.transportapp.form.FreighterForm;
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

    public List<Order> getOrdersFilteredByOrderNumber(String searchStr) {

        return orderRepository.findAllByOrderNumberContains(searchStr);
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

    public Freighter getFreighterById(Long id) {
        Optional<Freighter> optionalFreighter = freighterRepository.findById(id);
        if (optionalFreighter.isPresent()) {
            return optionalFreighter.get();
        }
        return null;
    }

    public boolean saveOrder(OrderForm orderForm) {

        Order orderNew = new Order();

        orderNew.setLoadDate(orderForm.getLoadDate());
        // sprawdanie czy order number ma minimum 3 znaki
        String orderPrefix = orderForm.getOrderNumber();
        if (orderPrefix.length() >= 3) {
            orderPrefix = orderPrefix.substring(0, 3);
        } else {
            return false;
        }
        // sprawdzenie czy istnieje prefix fabryki dla podanego order number
        Optional<Factory> cityPrefix = factoryRepository.findFactoryByPrefixContains(orderPrefix);
        if (cityPrefix.isPresent()) {
            orderNew.setFactory(cityPrefix.get());
            orderNew.setOrderNumber(orderForm.getOrderNumber());
        } else {
            return false;
        }
        // splitowanie kodów po przecinku do linked listy
        List<Code> codes = new LinkedList<Code>();
        String[] stringCodes = orderForm.getCityCode().split(",");
        for (int i = 0; i < stringCodes.length; i++) {
            Code code = new Code();
            code.setCityCode(stringCodes[i]);
            codes.add(code);
        }
        orderNew.setCodes(codes);

        orderNew.setPrice(orderForm.getPrice());
        orderNew.setFreighterPrice(orderForm.getFreighterPrice());
        orderNew.setUser(orderForm.getUser());
        orderRepository.save(orderNew);
        return true;
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

    public void saveFreighter(FreighterForm freighterForm) {
        Freighter freighterNew = new Freighter();
        freighterNew.setFreighterEmail(freighterForm.getFreighterEmail());
        freighterNew.setFreighterInfo(freighterForm.getFreighterInfo());
        freighterNew.setFreighterName(freighterForm.getFreighterName());
        freighterNew.setFreighterPerson(freighterForm.getFreighterPerson());
        freighterNew.setFreighterPhone(freighterForm.getFreighterPhone());

        freighterRepository.save(freighterNew);
    }

    public void updateOrder(Long id, OrderForm orderForm) {

        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.get().setOrderNumber(orderForm.getOrderNumber());
        optionalOrder.get().setLoadDate(orderForm.getLoadDate());
        //update kodow - splitowanie stringa do linked listy
        List<Code> codes = new LinkedList<Code>();
        String[] stringCodes = orderForm.getCityCode().split(",");
        for (int i = 0; i < stringCodes.length; i++) {
            Code code = new Code();
            code.setCityCode(stringCodes[i]);
            codes.add(code);
        }
        optionalOrder.get().setCodes(codes);

        optionalOrder.get().setPrice(orderForm.getPrice());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());
        optionalOrder.get().setFreighter(orderForm.getFreighter());
        optionalOrder.get().setUser(orderForm.getUser());

        orderRepository.save(optionalOrder.get());
    }

    public void updateFactory(Long id, FactoryForm factoryForm) {

        Optional<Factory> optionalFactory = factoryRepository.findById(id);
        optionalFactory.get().setPrefix(factoryForm.getPrefix());
        optionalFactory.get().setFactoryName(factoryForm.getFactoryName());
        optionalFactory.get().setFactoryCity(factoryForm.getFactoryCity());
        optionalFactory.get().setFactoryAddress(factoryForm.getFactoryAddress());
        optionalFactory.get().setFactoryContact(factoryForm.getFactoryContact());
        optionalFactory.get().setFactoryInfo(factoryForm.getFactoryInfo());

        factoryRepository.save(optionalFactory.get());
    }

    public void updateFreighter(Long id, FreighterForm freighterForm) {

        Optional<Freighter> optionalFreighter = freighterRepository.findById(id);
        optionalFreighter.get().setFreighterEmail(freighterForm.getFreighterEmail());
        optionalFreighter.get().setFreighterInfo(freighterForm.getFreighterInfo());
        optionalFreighter.get().setFreighterName(freighterForm.getFreighterName());
        optionalFreighter.get().setFreighterPerson(freighterForm.getFreighterPerson());
        optionalFreighter.get().setFreighterPhone(freighterForm.getFreighterPhone());

        freighterRepository.save(optionalFreighter.get());
    }

}
