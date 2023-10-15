package it.numble.hittracker.repository;

import it.numble.hittracker.entity.UrlBeingTracked;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlBeingTracked, Long> {

    boolean existsByUrl(String url);
    Optional<UrlBeingTracked> findByUrl(String url);
}
