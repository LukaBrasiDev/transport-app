package pl.lukabrasi.transportapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.form.*;
import pl.lukabrasi.transportapp.model.*;
import pl.lukabrasi.transportapp.repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final UserRepository userRepository;
    final FreighterRepository freighterRepository;
    final FactoryRepository factoryRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, FreighterRepository freighterRepository, FactoryRepository factoryRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.freighterRepository = freighterRepository;
        this.factoryRepository = factoryRepository;
    }

    public enum ActionResponse {
        SUCCESS,
        ERROR,
        DUPLICAT,
        EDIT,
        NOFACTORY
    }

    public Page<Order> getOrders(Pageable pageable) {

        return orderRepository.findAllByOrderByLoadDateDescLoadingCityAsc(pageable);
    }

    public Page<Order> getOrderById(Pageable pageable) {

        return orderRepository.findAllByOrderByIdDesc(pageable);
    }

    public Page<Order> findCurrentWeekNotSold(Pageable pageable) {
        return orderRepository.findCurrentWeekNotSold(pageable);
    }

    public Page<Order> findCurrentWeekAll(Pageable pageable) {
        return orderRepository.findCurrentWeekAll(pageable);
    }

    public Page<Order> getOrdersInRange(LocalDate date1, LocalDate date2, Pageable pageable) {

        return orderRepository.findByLoadDateBetweenOrderByLoadDateAscLoadingCityAsc(date1, date2, pageable);
    }

    public Page<Order> getOrdersInRangeNotSold(LocalDate date1, LocalDate date2, Pageable pageable) {

        return orderRepository.findNotSoldOrdersInRange(date1, date2, pageable);
    }

/*    public List<Order> getOrdersFilteredByOrderNumber(String searchStr) {

        return orderRepository.findAllByOrderNumberContains(searchStr);
    }*/

    public List<Factory> getFactories() {
        return factoryRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Freighter> getFreighters() {
        return freighterRepository.findAll();
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

    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public ActionResponse saveOrder(OrderForm orderForm) {

        Order orderNew = new Order();
        String orderNb = orderForm.getOrderNumber()
                .toUpperCase()
                .trim()
                .replace(" ", "");
        if (orderRepository.existsByOrderNumber(orderNb)) {
            return ActionResponse.DUPLICAT;
        }
        orderNew.setLoadDate(orderForm.getLoadDate());
        orderNew.setOrderNumber(orderNb);
        // sprawdanie czy order number ma minimum 3 znaki
        String orderPrefix = orderNb;
        if (orderPrefix.length() >= 3) {
            orderPrefix = orderPrefix.substring(0, 3);
        } else {
            return ActionResponse.ERROR;
        }
        // sprawdzenie czy istnieje prefix fabryki dla podanego order number
        Optional<Factory> cityPrefix = factoryRepository.findFactoryByPrefixContains(orderPrefix);
        if (cityPrefix.isPresent()) {
            orderNew.setFactory(cityPrefix.get());
            orderNew.setLoadingCity(cityPrefix.get().getFactoryCity());
        } else {
            return ActionResponse.NOFACTORY;
        }
        // splitowanie kodów po przecinku do linked listy
        List<String> codes = new LinkedList<>();
        String[] stringCodes = Arrays.asList(orderForm.getCityCodes().split("[,]")).stream().filter(str -> !str.trim().isEmpty()).collect(Collectors.toList()).toArray(new String[0]);
        for (int i = 0; i < stringCodes.length; i++) {
            codes.add(stringCodes[i]);
        }
        orderNew.setCityCodes(codes.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replace(" ", "")
                .replaceAll("\\s{2,}", "")
                .replace(",", ", ")
                //.replaceAll("[^\\x00-\\x7F]", "")
                .trim());
        //orderNew.setOurNumber(orderForm.getOurNumber());
        orderNew.setPrice(orderForm.getPrice());
        orderNew.setFreighterPrice(orderForm.getFreighterPrice());
        orderNew.setUser(orderForm.getUser());

        orderRepository.save(orderNew);
        return ActionResponse.SUCCESS;
    }


    public void saveFactory(FactoryForm factoryForm) {
        Factory factoryNew = new Factory();
        factoryNew.setPrefix(factoryForm.getPrefix());
        factoryNew.setFactoryName(factoryForm.getFactoryName());
        factoryNew.setFactoryCity(factoryForm.getFactoryCity().toUpperCase());
        factoryNew.setFactoryAddress(factoryForm.getFactoryAddress());
        factoryNew.setFactoryContact(factoryForm.getFactoryContact());
        factoryNew.setFactoryInfo(factoryForm.getFactoryInfo());
        if (!factoryForm.getFactoryCity().isEmpty()) {
            factoryRepository.save(factoryNew);
        }
    }

    public void saveFreighter(FreighterForm freighterForm) {
        Freighter freighterNew = new Freighter();
        freighterNew.setFreighterEmail(freighterForm.getFreighterEmail());
        freighterNew.setFreighterInfo(freighterForm.getFreighterInfo());
        freighterNew.setFreighterName(freighterForm.getFreighterName());
        freighterNew.setFreighterPerson(freighterForm.getFreighterPerson());
        freighterNew.setFreighterPhone(freighterForm.getFreighterPhone());
        if (!freighterForm.getFreighterName().isEmpty()) {
            freighterRepository.save(freighterNew);
        }
    }

    public void saveUser(UserForm userForm) {
        User userNew = new User();
        userNew.setUserName(userForm.getUserName());
        userNew.setEmail(userForm.getEmail());
        userNew.setTelephone(userForm.getTelephone());
        // userNew.setPassword(userForm.getFreighterPerson());
        if (!userForm.getUserName().isEmpty()) {
            userRepository.save(userNew);
        }
    }

    public ActionResponse updateOrder(Long id, OrderForm orderForm) {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        optionalOrder.get().setOrderNumber(orderForm.getOrderNumber());

        String orderPrefix = orderForm.getOrderNumber();
        if (orderPrefix.length() >= 3) {
            orderPrefix = orderPrefix.substring(0, 3);
            // sprawdzenie czy istnieje prefix fabryki dla podanego order number
            Optional<Factory> cityPrefix = factoryRepository.findFactoryByPrefixContains(orderPrefix);
            //uzupelnianie fabryki na podstawie prefixu tury
            if (cityPrefix.isPresent()) {
                optionalOrder.get().setFactory(cityPrefix.get());
            }
            //uzupelnianie zaladunku jezeli jest pusty miastem fabryki z tury
            if (cityPrefix.isPresent() && optionalOrder.get().getLoadingCity() == null) {
                optionalOrder.get().setLoadingCity(cityPrefix.get().getFactoryCity());
            } else {
                //tura inna niz z factory - nadpisz miasto
                optionalOrder.get().setLoadingCity(orderForm.getLoadingCity().toUpperCase());
            }
        } else {
            //brak tury
            optionalOrder.get().setLoadingCity(orderForm.getLoadingCity().toUpperCase());
        }

        optionalOrder.get().setLoadDate(orderForm.getLoadDate());
        //update kodow - splitowanie stringa do linked listy
        List<String> codes = new LinkedList<>();
        String[] stringCodes = Arrays.asList(orderForm.getCityCodes().split("[,]")).stream().filter(str -> !str.trim().isEmpty()).collect(Collectors.toList()).toArray(new String[0]);
        for (int i = 0; i < stringCodes.length; i++) {
            codes.add(stringCodes[i]);
        }
        optionalOrder.get().setCityCodes(codes.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replace(" ", "")
                .replaceAll("\\s{2,}", "")
                .replace(",", ", ")
                // .replaceAll("[^\\x00-\\x7F]", "")
                .trim());
        if (!orderForm.getOurNumber().isEmpty()){
        optionalOrder.get().setOurNumber(orderForm.getOurNumber());
        }
        optionalOrder.get().setPrice(orderForm.getPrice());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());
        optionalOrder.get().setFreighter(orderForm.getFreighter());
        optionalOrder.get().setUser(orderForm.getUser());

        orderRepository.save(optionalOrder.get());
        return ActionResponse.EDIT;
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

    public void updateUser(Long id, UserForm userForm) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.get().setUserName(userForm.getUserName());
        optionalUser.get().setEmail(userForm.getEmail());
        optionalUser.get().setTelephone(userForm.getTelephone());
        //optionalUser.get().setPassword(userForm.getPassword());

        userRepository.save(optionalUser.get());
    }

    public List<Integer> soldByMtwInCurrentMonth(LocalDate date1){

        return orderRepository.soldByMtwCurrentMonth(date1);
    }

    public List<Order> getMonthRaportByPerson (LocalDate loadDate, int person){
        return orderRepository.monthRaportByPerson(loadDate,person);
    }

  /*  public int soldInCurrentMonth(LocalDate date1){

        return orderRepository.soldInCurrentMonth(date1);
    }*/

}
