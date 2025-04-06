package UTCN_IMDB.demo.repository;

import UTCN_IMDB.demo.model.Lists;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListsRepository extends JpaRepository<Lists, UUID> {

}
