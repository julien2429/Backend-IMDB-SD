package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.ForgottenPasswordData;
import UTCN_IMDB.demo.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ForgottenPasswordRepository  extends JpaRepository<ForgottenPasswordData, UUID> {

    Optional<ForgottenPasswordData> findByForgottenPasswordToken(String token);

}
