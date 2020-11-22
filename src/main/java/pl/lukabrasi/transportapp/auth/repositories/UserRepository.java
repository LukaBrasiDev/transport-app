package pl.lukabrasi.transportapp.auth.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lukabrasi.transportapp.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {//<nazwa encji, typ id>

    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);

    @Query(value = "select * from user where active is true", nativeQuery = true)
    List<User> finadAllUsers();

}
