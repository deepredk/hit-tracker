package it.numble.hittracker.scheduler;

import it.numble.hittracker.service.HitTrackerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyHitLogScheduler {

    private final HitTrackerService hitTrackerService;

    public DailyHitLogScheduler(HitTrackerService hitTrackerService) {
        this.hitTrackerService = hitTrackerService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void logDailyHit() {
        hitTrackerService.tomorrow();
    }
}
