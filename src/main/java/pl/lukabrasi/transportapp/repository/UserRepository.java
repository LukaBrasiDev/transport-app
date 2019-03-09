package pl.lukabrasi.transportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lukabrasi.transportapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
