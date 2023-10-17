package it.numble.hittracker.scheduler;

import it.numble.hittracker.service.HitTrackerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class DailyHitLogScheduler {

    private final HitTrackerService hitTrackerService;
    private final Clock clock;

    public DailyHitLogScheduler(HitTrackerService hitTrackerService, Clock clock) {
        this.hitTrackerService = hitTrackerService;
        this.clock = clock;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void logDailyHit() {
        hitTrackerService.tomorrow(clock);
    }
}
