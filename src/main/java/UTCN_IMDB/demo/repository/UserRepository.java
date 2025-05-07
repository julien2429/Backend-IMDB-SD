package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);


    Optional<User> getUserByEmail(String email);
}
