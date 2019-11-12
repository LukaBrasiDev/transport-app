package pl.lukabrasi.transportapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.lukabrasi.transportapp.form.*;
import pl.lukabrasi.transportapp.model.*;
import pl.lukabrasi.transportapp.repository.*;

import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final UserRepository userRepository;
    final FreighterRepository freighterRepository;
    final FactoryRepository factoryRepository;
    final BanRepository banRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, FreighterRepository freighterRepository, FactoryRepository factoryRepository, BanRepository banRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.freighterRepository = freighterRepository;
        this.factoryRepository = factoryRepository;
        this.banRepository = banRepository;
    }

    public enum ActionResponse {
        SUCCESS,
        ERROR,
        DUPLICAT,
        EDIT,
        NOFACTORY,
        ZAKAZOK,
        ZAKAZNOK
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

    public Page<Order> findPreviousWeekNotSold(Pageable pageable) {
        return orderRepository.findPreviousWeekNotSold(pageable);
    }

    public Page<Order> findPreviousWeekAll(Pageable pageable) {

        return orderRepository.findPreviousWeekAll(pageable);
    }

    public Page<Order> findNextWeekNotSold(Pageable pageable) {
        return orderRepository.findNextWeekNotSold(pageable);
    }

    public Page<Order> findNextWeekAll(Pageable pageable) {

        return orderRepository.findNextWeekAll(pageable);
    }


    public Page<Order> getOrdersInRange(LocalDate date1, LocalDate date2, Pageable pageable) {

        return orderRepository.findByLoadDateBetweenOrderByLoadDateAscLoadingCityAsc(date1, date2, pageable);
    }

    public Page<Order> getOrdersInRangeNotSold(LocalDate date1, LocalDate date2, Pageable pageable) {

        return orderRepository.findNotSoldOrdersInRange(date1, date2, pageable);
    }

    public Page<Order> findAllByOrderNumberContains(String searchStr, Pageable pageable) {

        return orderRepository.findAllByOrderNumberContainsOrderByLoadDateAscLoadingCityAsc(searchStr, pageable);
    }

    public List<Factory> getFactories() {
        return factoryRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Freighter> getFreighters() {
        return freighterRepository.findAll();
    }

    public List<Freighter> getFreightersSorted() {
        return freighterRepository.findAllByOrderByFreighterNameAsc();
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

    public ActionResponse saveOrder(OrderForm orderForm) throws UnknownHostException {

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
        orderNew.setQueryTime(LocalDateTime.now());
//pobieranie adresu ip
        //         InetAddress inetAddress = InetAddress.getLocalHost();
        // String username = System.getProperty("user.name" ).toString();
//String ipAddress = inetAddress.toString();
        //orderNew.setIpaddress(System.getProperty("user.name" ));

        // update adresu ip
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        orderNew.setIpaddress(remoteAddress);

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
        factoryNew.setFactoryGroup(factoryForm.getFactoryGroup());
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

    public ActionResponse updateOrder(Long id, OrderForm orderForm) throws UnknownHostException {

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
        if (!orderForm.getOurNumber().isEmpty()) {
            optionalOrder.get().setOurNumber(orderForm.getOurNumber());
        }
        optionalOrder.get().setPrice(orderForm.getPrice());
        optionalOrder.get().setLoadHour(orderForm.getLoadHour());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());
        optionalOrder.get().setFreighter(orderForm.getFreighter());
        optionalOrder.get().setUser(orderForm.getUser());
        optionalOrder.get().setQueryTime(LocalDateTime.now());
        // update adresu ip
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        optionalOrder.get().setIpaddress(remoteAddress);

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
        optionalFactory.get().setFactoryGroup(factoryForm.getFactoryGroup());
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

    public List<Integer> soldByMtwInCurrentMonth(LocalDate date1) {

        return orderRepository.soldByMtwCurrentMonth(date1);
    }

    public List<Order> getMonthRaportByPerson(LocalDate loadDate, int person) {
        return orderRepository.monthRaportByPerson(loadDate, person);
    }

    public List<LocalDate> getMonthYear() {

        int size = orderRepository.getMonthYear().size();
        List<String> lista = orderRepository.getMonthYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        List<LocalDate> input = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            input.add(LocalDate.parse(lista.get(i), formatter));
        }
        return input;

    }


    //trzy raporty tygodniowe
    public Integer getBegaWeekly(LocalDate date1) {
        if (orderRepository.soldBegaGroupWeekly(date1) > 0) {
            return orderRepository.soldBegaGroupWeekly(date1);

        } else return 0;
    }

    public Integer getWojcikWeekly(LocalDate date1) {
        if (orderRepository.soldWojcikGroupWeekly(date1) > 0) {
            return orderRepository.soldWojcikGroupWeekly(date1);
        } else return 0;
    }

    public Integer getOtherWeekly(LocalDate date1) {
        if (orderRepository.soldOtherGroupWeekly(date1) > 0) {
            return orderRepository.soldOtherGroupWeekly(date1);
        } else return 0;
    }

    public List<Ban> getBansSorted() {
        return banRepository.findAllByOrderByStatusDescFreighterAsc();
    }

    public ActionResponse saveBan(BanForm banForm) {
        Ban banNew = new Ban();


        // Order orderNew = new Order();
        String freighter = banForm.getFreighter()
                .toUpperCase()
                .trim();
        if (banRepository.existsByFreighter(freighter)) {
            return ActionResponse.ZAKAZNOK;
        }
        banNew.setFreighter(freighter);
        banNew.setCity(banForm.getCity());
        banNew.setNip(banForm.getNip());
        banNew.setDescription(banForm.getDescription());
        banNew.setStatus("ZAKAZ");
        banNew.setQueryTime(LocalDateTime.now());

        // userNew.setPassword(userForm.getFreighterPerson());
        if (!banForm.getFreighter().isEmpty()) {
            banRepository.save(banNew);
            return ActionResponse.ZAKAZOK;
        }
        return ActionResponse.ZAKAZNOK;
    }

    public void updateBanStatus(Long id, BanForm banForm) {

        Optional<Ban> optionalBan = banRepository.findById(id);
        // optionalBan.get().setFreighter(banForm.getFreighter());
        //optionalBan.get().setCity(banForm.getCity());
        //optionalBan.get().setNip(banForm.getNip());

        String test = optionalBan.get().getStatus();
        boolean isFound = test.contains("ZAKAZ"); // true


        if (isFound == true) {
            optionalBan.get().setStatus("OK");

        } else {
            optionalBan.get().setStatus("ZAKAZ");
        }

        optionalBan.get().setQueryTime(LocalDateTime.now());
        //   optionalBan.get().setFreighterPhone(banForm.getFreighterPhone());

        banRepository.save(optionalBan.get());
    }

    public Ban getBanById(Long id) {
        Optional<Ban> optionalBan = banRepository.findById(id);
        if (optionalBan.isPresent()) {
            return optionalBan.get();
        }
        return null;
    }

    public void deleteBanById(Long id) {

        banRepository.deleteById(id);

    }


}
