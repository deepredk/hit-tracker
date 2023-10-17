package it.numble.hittracker.repository;

import it.numble.hittracker.entity.Url;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {

    boolean existsByUrl(String url);
    Optional<Url> findByUrl(String url);
}
