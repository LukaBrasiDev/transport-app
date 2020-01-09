package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lukabrasi.transportapp.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from user order by active desc", nativeQuery = true)
    List<User> finadAllUsers();

  //  @Query(value = "select * from user where active = 1", nativeQuery = true)
  //  List<User> finadAllUsersSortedAndFiltered();




}
