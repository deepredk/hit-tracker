package it.numble.hittracker.repository;

import it.numble.hittracker.entity.DailyHitLog;
import java.util.List;

import it.numble.hittracker.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyHitLogRepository extends JpaRepository<DailyHitLog, Long> {

    List<DailyHitLog> findAllByUrl(Url url);
}
