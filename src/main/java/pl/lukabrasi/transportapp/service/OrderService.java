package pl.lukabrasi.transportapp.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.auth.repositories.UserRepository;
import pl.lukabrasi.transportapp.dto.ReportMonthPerson;
import pl.lukabrasi.transportapp.form.*;
import pl.lukabrasi.transportapp.model.*;
import pl.lukabrasi.transportapp.repository.*;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final UserRepository userRepository;
    final FreighterRepository freighterRepository;
    final FactoryRepository factoryRepository;
    final BanRepository banRepository;
    final OurDriverRepository ourDriverRepository;
    final FreighterBaseRepository freighterBaseRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, FreighterRepository freighterRepository, FactoryRepository factoryRepository, BanRepository banRepository, OurDriverRepository ourDriverRepository, FreighterBaseRepository ourFreighterBaseRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.freighterRepository = freighterRepository;
        this.factoryRepository = factoryRepository;
        this.banRepository = banRepository;
        this.ourDriverRepository = ourDriverRepository;
        this.freighterBaseRepository = ourFreighterBaseRepository;
    }




    public enum ActionResponse {
        SUCCESS,
        ERROR,
        DUPLICAT,
        EDIT,
        NOFACTORY,
        ZAKAZOK,
        ZAKAZNOK,
        OK
    }

    public Page<Order> getOrders(Pageable pageable) {

        return orderRepository.findAllByOrderByLoadDateDescLoadingCityAsc(pageable);
    }

    public Page<Order> getNotconfirmedPrices(Pageable pageable) {

        return orderRepository.findNotConfirmedPrices(pageable);
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

    public Page<Order> findCurrentWeekMTW(Pageable pageable) {

        return orderRepository.findCurrentWeekMTW(pageable);
    }

    public Page<Order> findCurrentWeekMTWempty(Pageable pageable) {

        return orderRepository.findCurrentWeekMTWempty(pageable);
    }

    public Page<Order> find2PreviousWeekMTW(Pageable pageable) {

        return orderRepository.find2PreviousWeekMTW(pageable);
    }

    public Page<Order> find3PreviousWeekMTW(Pageable pageable) {

        return orderRepository.find3PreviousWeekMTW(pageable);
    }


    public Page<Order> findPreviousWeekMTW(Pageable pageable) {

        return orderRepository.findPreviousWeekMTW(pageable);
    }

    public Page<Order> findNextWeekMTW(Pageable pageable) {

        return orderRepository.findNextWeekMTW(pageable);
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


    public List<Order> getOrdersInRange(LocalDate date1, LocalDate date2) {

        return orderRepository.findByLoadDateBetweenOrderByLoadDateAscLoadingCityAsc(date1, date2);
    }

    public List<Order> findMTWOrdersInRange(LocalDate date1, LocalDate date2) {

        return orderRepository.findMTWOrdersInRange(date1, date2);
    }

    public List<Order> getOrdersInRangeNotSold(LocalDate date1, LocalDate date2) {

        return orderRepository.findNotSoldOrdersInRange(date1, date2);
    }

    public Page<Order> findAllByOrderNumberContains(String searchStr, Pageable pageable) {

        return orderRepository.findAllByOrderNumberContainsOrderByLoadDateAscLoadingCityAsc(searchStr, pageable);
    }

    public Page<Order> findAllMTW(Pageable pageable) {
        return orderRepository.findAllMTW(pageable);
    }

    public List<Factory> getFactories() {
        return factoryRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.finadAllUsers();
    }


    public List<OurDriver> getOurDrivers() {
        return ourDriverRepository.findAllByOrderByDriverSurnameAscAndActiveDesc();
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

    public OurDriver getOurDriverById(Long id) {
        Optional<OurDriver> optionalOurDriver = ourDriverRepository.findById(id);
        if (optionalOurDriver.isPresent()) {
            return optionalOurDriver.get();
        }
        return null;
    }

    public ActionResponse saveOrder(OrderForm orderForm, String loggedUser) throws UnknownHostException {

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
        String[] stringCodes = Arrays.asList(orderForm.getCityCodes().split("[,]")).stream().filter(str -> !str.trim().isEmpty()).collect(toList()).toArray(new String[0]);
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
        orderNew.setComment(orderForm.getComment());
        orderNew.setKilometers(orderForm.getKilometers());
        orderNew.setPrice(orderForm.getPrice());
        orderNew.setPriceConfirmed(orderForm.getPriceConfirmed());
        orderNew.setFreighterPrice(orderForm.getFreighterPrice());
        orderNew.setUser(orderForm.getUser());
        orderNew.setQueryTime(LocalDateTime.now());
//pobieranie adresu ip
        //         InetAddress inetAddress = InetAddress.getLocalHost();
        // String username = System.getProperty("user.name" ).toString();
//String ipAddress = inetAddress.toString();
        //orderNew.setIpaddress(System.getProperty("user.name" ));

        // update adresu ip
        //  String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        //       .getRequest().getRemoteAddr();
        orderNew.setIpaddress(loggedUser);

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

    public ActionResponse saveFreighter(FreighterForm freighterForm) {
        Freighter freighterNew = new Freighter();
        freighterNew.setFreighterEmail(freighterForm.getFreighterEmail());
        freighterNew.setFreighterInfo(freighterForm.getFreighterInfo());
        freighterNew.setFreighterName(freighterForm.getFreighterName());
        freighterNew.setFreighterPerson(freighterForm.getFreighterPerson());
        freighterNew.setFreighterPhone(freighterForm.getFreighterPhone());
        if (freighterRepository.findFreighterByFreighterName(freighterNew.getFreighterName()).isPresent()) {
            return ActionResponse.DUPLICAT;
        }
        if (freighterForm.getFreighterName().isEmpty()) {
            return ActionResponse.ERROR;

        }
        freighterRepository.save(freighterNew);
        return ActionResponse.SUCCESS;
    }

    public ActionResponse saveUser(UserForm userForm) {
        User userNew = new User();
        if (userRepository.existsByUserName(userForm.getUserName())) {
            return ActionResponse.ERROR;
        }
        userNew.setUserName(userForm.getUserName());
        userNew.setEmail(userForm.getEmail());
        userNew.setTelephone(userForm.getTelephone());
        // userNew.setPassword(userForm.getFreighterPerson());
        userNew.setActive(true);
        if (!userForm.getUserName().isEmpty()) {
            userRepository.save(userNew);
            return ActionResponse.SUCCESS;
        }
        return ActionResponse.ERROR;
    }

    public ActionResponse saveOurDriver(OurDriverForm ourDriverForm) {
        OurDriver ourDriverNew = new OurDriver();
        ourDriverNew.setDriverName(ourDriverForm.getDriverName());
        ourDriverNew.setDriverSurname(ourDriverForm.getDriverSurname());
        ourDriverNew.setDriverCar(ourDriverForm.getDriverCar());
        ourDriverNew.setDriverSemitrailer(ourDriverForm.getDriverSemitrailer());
        ourDriverNew.setUser(ourDriverForm.getUser());
        if (ourDriverRepository.existsByDriverNameAndDriverSurname(ourDriverNew.getDriverName(), ourDriverNew.getDriverSurname())) {
            return ActionResponse.DUPLICAT;
        }
        ourDriverNew.setActive(true);
        if (ourDriverForm.getDriverSurname().isEmpty() &&
                ourDriverForm.getDriverName().isEmpty()) {
            return ActionResponse.ERROR;
        }
        ourDriverRepository.save(ourDriverNew);
        return ActionResponse.SUCCESS;
    }

    public ActionResponse updateOrder(Long id, OrderForm orderForm, String loggedUser) throws UnknownHostException {

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
        String[] stringCodes = Arrays.asList(orderForm.getCityCodes().split("[,]")).stream().filter(str -> !str.trim().isEmpty()).collect(toList()).toArray(new String[0]);
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
        optionalOrder.get().setComment(orderForm.getComment());
        optionalOrder.get().setKilometers(orderForm.getKilometers());
        optionalOrder.get().setPrice(orderForm.getPrice());
        if (orderForm.getPrice() != null) {
            optionalOrder.get().setPriceConfirmed(orderForm.getPriceConfirmed());
        }
        optionalOrder.get().setLoadHour(orderForm.getLoadHour());
        optionalOrder.get().setFreighterPrice(orderForm.getFreighterPrice());


        if ((orderForm.getFreighter().isEmpty() && orderForm.getUser() != null && !orderForm.getUser().getUserName().equals("STORNO")) || (!orderForm.getFreighter().isEmpty() && orderForm.getUser() == null)) {
            return ActionResponse.ERROR;
        }

        //tak bylo:  optionalOrder.get().setFreighter(orderForm.getFreighter());


        //sprawdzam czytaki przewoznik istnieje w bazie danych
        String namePrzewoznika = orderForm.getFreighter();
        Optional<Freighter> freighterOptional = freighterRepository.findFreighterByFreighterName(orderForm.getFreighter());


        //jezeli przewoznik nie istnieje w bazie to zapisuje go do bazy jako nowy
        if (!freighterOptional.isPresent()) {
            //  contactOptional.get().getTags().add(tagRepository.save(new Tag(t)));
            Freighter freighterNew = new Freighter();
            freighterNew.setFreighterName(orderForm.getFreighter());
            freighterRepository.save(freighterNew);
            optionalOrder.get().setFreighter(freighterNew);
            //jezeli tag istnieje w bazie to tylko dodaje go do kontaktu
        } else {
            // contactOptional.get().getTags().add(tagOptional.get());
            optionalOrder.get().setFreighter(freighterRepository.findByFreighterName(orderForm.getFreighter()));
        }
        optionalOrder.get().setDriver(orderForm.getDriver());
        optionalOrder.get().setUser(orderForm.getUser());
        // setowanie pustych wartości dla importu jezeli przeweznik zmieniony z MTW na innego
        if (!orderForm.getFreighter().equals("MTW")) {
            optionalOrder.get().setDriver(null);
        }

        optionalOrder.get().setQueryTime(LocalDateTime.now());
        // update adresu ip
        //String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        //          .getRequest().getRemoteAddr();
        //  optionalOrder.get().setIpaddress(remoteAddress);

        //update kto ostatni zmodyfikował
        //UserSession loggedUser = new UserSession();
        // loggedUser.getUserEntity().setUserName();
        optionalOrder.get().setIpaddress(loggedUser);

        orderRepository.save(optionalOrder.get());
        return ActionResponse.EDIT;
    }

    public ActionResponse updateOrderImport(Long id, OrderForm orderForm, String loggedUser) throws UnknownHostException {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (orderForm.getDriver() != null) {
            optionalOrder.get().setDriver(orderForm.getDriver());
        }
        //import
        if (orderForm.getDocDateExp() != null) {
            optionalOrder.get().setDocDateExp(orderForm.getDocDateExp());
        }
        if (orderForm.getExportEnd() != null) {
            optionalOrder.get().setExportEnd(orderForm.getExportEnd());
        }
        if (orderForm.getLoadingCityImp() != null) {
            optionalOrder.get().setLoadingCityImp(orderForm.getLoadingCityImp().trim());
        }
        if (orderForm.getKilometersImp() != null) {
            optionalOrder.get().setKilometersImp(orderForm.getKilometersImp());
        }
        if (orderForm.getCityCodesImp() != null) {
            optionalOrder.get().setCityCodesImp(orderForm.getCityCodesImp());
        }
        if (orderForm.getDocDateImp() != null) {
            optionalOrder.get().setDocDateImp(orderForm.getDocDateImp());
        }
        if (orderForm.getNextLoadingCityImp() != null) {
            optionalOrder.get().setNextLoadingCityImp(orderForm.getNextLoadingCityImp());
        }
        if (orderForm.getPriceImp() != null) {
            optionalOrder.get().setPriceImp(orderForm.getPriceImp());
        }
        if (orderForm.getUserImp() != null) {
            optionalOrder.get().setUserImp(orderForm.getUserImp());
        }

        optionalOrder.get().setQueryTimeImp(LocalDateTime.now());
        // update adresu ip
        //   String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        //         .getRequest().getRemoteAddr();
        optionalOrder.get().setIpaddressImp(loggedUser);

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


    public void updateOurDriver(Long id, OurDriverForm ourDriverForm) {
        Optional<OurDriver> optionalOurDriver = ourDriverRepository.findById(id);
        optionalOurDriver.get().setDriverName(ourDriverForm.getDriverName());
        optionalOurDriver.get().setDriverSurname(ourDriverForm.getDriverSurname());
        optionalOurDriver.get().setDriverCar(ourDriverForm.getDriverCar());
        optionalOurDriver.get().setDriverSemitrailer(ourDriverForm.getDriverSemitrailer());
        optionalOurDriver.get().setUser(ourDriverForm.getUser());
        optionalOurDriver.get().setActive(ourDriverForm.getActive());
        //optionalUser.get().setPassword(userForm.getPassword());

        ourDriverRepository.save(optionalOurDriver.get());
    }

    public List<Integer> soldByMtwInCurrentMonth(LocalDate date1) {

        return orderRepository.soldByMtwCurrentMonth(date1);
    }

    // public List<Order> getMonthRaportByPerson(LocalDate loadDate, Integer person) {
    //  return orderRepository.monthRaportByPerson(loadDate, person);
    //  }

    public List<Order> getMonthRaportByDriver(LocalDate loadDate1, LocalDate loadDate2, Integer person) {
        return orderRepository.monthRaportByDriver(loadDate1, loadDate2, person);
    }

    public List<Order> getMonthRaportByallDrivers(LocalDate loadDate1, LocalDate loadDate2) {
        return orderRepository.monthRaportByAllDrivers(loadDate1, loadDate2);
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

    public List<FreighterBase> getFreighterBaseSorted() {
        return freighterBaseRepository.findAllByOrderByNameAsc();
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

    public void updateBan(Long id, BanForm banForm) {

        Optional<Ban> optionalBan = banRepository.findById(id);
     //   optionalBan.get().setFreighter(banForm.getFreighter());
        optionalBan.get().setCity(banForm.getCity());
        optionalBan.get().setNip(banForm.getNip());
        optionalBan.get().setDescription(banForm.getDescription());
        optionalBan.get().setStatus(banForm.getStatus());

       // optionalBan.get().setQueryTime(LocalDateTime.now());
        //   optionalBan.get().setFreighterPhone(banForm.getFreighterPhone());

        banRepository.save(optionalBan.get());
    }

    public ActionResponse saveFreighterBase(FreighterBaseForm freighterBaseForm) {
        FreighterBase freighterBaseNew = new FreighterBase();


        // Order orderNew = new Order();
        String name = freighterBaseForm.getName()
                .toUpperCase()
                .trim();
        if (freighterBaseRepository.existsByName(name)) {
            return ActionResponse.ZAKAZNOK;
        }
        freighterBaseNew.setName(name);
        freighterBaseNew.setAddress(freighterBaseForm.getAddress());
        freighterBaseNew.setPerson(freighterBaseForm.getPerson());
        freighterBaseNew.setTelephone(freighterBaseForm.getTelephone());
        freighterBaseNew.setEmail(freighterBaseForm.getEmail());
        freighterBaseNew.setUser(freighterBaseForm.getUser());
        freighterBaseNew.setInfo(freighterBaseForm.getInfo());
        freighterBaseNew.setQueryTime(LocalDateTime.now());

        // userNew.setPassword(userForm.getFreighterPerson());
        if (!freighterBaseForm.getName().isEmpty()) {
            freighterBaseRepository.save(freighterBaseNew);
            return ActionResponse.OK;
        }
        return ActionResponse.ZAKAZNOK;
    }


   /* public void updateBanStatus(Long id, BanForm banForm) {

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
    }*/

    public void updateFreighterBase(Long id, FreighterBaseForm freighterBaseForm) {

        Optional<FreighterBase> optionalFreighterBase = freighterBaseRepository.findById(id);

        optionalFreighterBase.get().setName(freighterBaseForm.getName());
        optionalFreighterBase.get().setAddress(freighterBaseForm.getAddress());
        optionalFreighterBase.get().setPerson(freighterBaseForm.getPerson());
        optionalFreighterBase.get().setTelephone(freighterBaseForm.getTelephone());
        optionalFreighterBase.get().setEmail(freighterBaseForm.getEmail());
        optionalFreighterBase.get().setUser(freighterBaseForm.getUser());
        optionalFreighterBase.get().setInfo(freighterBaseForm.getInfo());
        optionalFreighterBase.get().setQueryTime(LocalDateTime.now());
        //   optionalBan.get().setFreighterPhone(banForm.getFreighterPhone());

        freighterBaseRepository.save(optionalFreighterBase.get());
    }

    public Ban getBanById(Long id) {
        Optional<Ban> optionalBan = banRepository.findById(id);
        if (optionalBan.isPresent()) {
            return optionalBan.get();
        }
        return null;
    }

    public FreighterBase getFreighterBaseById(Long id) {
        Optional<FreighterBase> optionalFreighterBase = freighterBaseRepository.findById(id);
        if (optionalFreighterBase.isPresent()) {
            return optionalFreighterBase.get();
        }
        return null;
    }

    public void deleteBanById(Long id) {

        banRepository.deleteById(id);

    }

    public void deleteFreighterBaseById(Long id) {

        freighterBaseRepository.deleteById(id);

    }

    public List<OurDriver> getDriversFreeWeek() {
        return ourDriverRepository.findDriversFreeWeek();
    }

    public List<OurDriver> getDriversFreeNextWeek() {
        return ourDriverRepository.findDriversFreeNextWeek();
    }

    public List<OurDriver> getDriversFreePreviousWeek() {
        return ourDriverRepository.findDriversFreePreviousWeek();
    }


    //RAPORTY
    public JasperPrint getMonthRaportByPerson(LocalDate loadDate, String person, String reportFormat) throws FileNotFoundException, JRException {
        String path = "C:\\Aplikacja_MTW\\raporty\\";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<ReportMonthPerson> employees = orderRepository.monthRaportByPerson(loadDate, person);
        //Load file an compiles it
        //File file = ResourceUtils.getFile("classpath:reports\\raportmiesieczny.jrxml");
        //new ClassPathResource(filename).getInputStream();
       // JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        InputStream employeeReportStream
                = getClass().getResourceAsStream("/reports/raportmiesieczny.jrxml");
        JasperReport jasperReport
                = JasperCompileManager.compileReport(employeeReportStream);
        //JRSaver.saveObject(jasperReport, "raportmiesieczny.jasper");


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> map = new HashMap<>();
        map.put("DATA_MIESIAC", loadDate.toString());
        map.put("SPEDYTOR", person);
        map.put("LOGO", getClass().getResourceAsStream("/reports/logo.png"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);

        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "raport_miesieczny_"+ person + "_" + loadDate.toString().substring(0, 7)+".pdf");
          }



        return jasperPrint;

    }




}
