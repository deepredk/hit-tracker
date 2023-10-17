package it.numble.hittracker.scheduler;

import it.numble.hittracker.service.HitTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@RequiredArgsConstructor
public class DailyHitLogScheduler {

    private final HitTrackerService hitTrackerService;
    private final Clock clock;

    @Scheduled(cron = "0 0 0 * * ?")
    public void logDailyHit() {
        hitTrackerService.tomorrow(clock);
    }
}
