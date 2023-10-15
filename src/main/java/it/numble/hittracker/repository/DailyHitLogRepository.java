package it.numble.hittracker.repository;

import it.numble.hittracker.entity.DailyHitLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyHitLogRepository extends JpaRepository<DailyHitLog, Long> {

}
