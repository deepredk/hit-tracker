package it.numble.hittracker.service;

import it.numble.hittracker.controller.dto.UrlHitInfoDto;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
import it.numble.hittracker.repository.IsCreatedUrlRepository;
import it.numble.hittracker.repository.DailyHitLogRepository;
import it.numble.hittracker.repository.HitRepository;
import it.numble.hittracker.repository.UrlRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HitTrackerService {

    private final UrlRepository urlRepository;
    private final DailyHitLogRepository dailyHitLogRepository;
    private final HitRepository hitRepository;
    private final IsCreatedUrlRepository isCreatedUrlRepository;

    public UrlHitInfoDto getHitInfo(String url) {
        if (!isExistsUrl(url)) {
            track(url);
        }

        return new UrlHitInfoDto(
            url,
            hitRepository.getTodayHit(url),
            hitRepository.getTotalHit(url)
        );
    }

    @Async
    public void hit(String url) {
        hitRepository.hit(url);

        if (!isExistsUrl(url)) {
            track(url);
        }
    }

    public List<DailyHitLog> getStatistics(String url) {
        Url savedUrl = urlRepository.findByUrl(url).orElseGet(() -> urlRepository.save(new Url(url)));
        return dailyHitLogRepository.findAllByUrl(savedUrl);
    }

    public void tomorrow(Clock clock) {
        LocalDate today = LocalDate.now(clock).minusDays(1);
        resetAndLogTodayHitOfAll(today);
        deleteOldDailyHitLogs(today);
    }

    private void resetAndLogTodayHitOfAll(LocalDate today) {
        List<Url> urls = urlRepository.findAll();
        for (Url url : urls) {
            int todayHit = hitRepository.getTodayHit(url.getUrl());
            DailyHitLog dailyHitLog = new DailyHitLog(today, todayHit, url);
            dailyHitLogRepository.save(dailyHitLog);

            hitRepository.deleteTodayHit(url.getUrl());
        }
    }

    private void deleteOldDailyHitLogs(LocalDate today) {
        List<DailyHitLog> logs = dailyHitLogRepository.findAll();
        logs.stream()
                .filter(log -> log.isSevenDaysAgo(today))
                .forEach(dailyHitLogRepository::delete);
    }

    @Async
    private void track(String url) {
        Url urlBeingTracked = new Url(url);
        urlRepository.save(urlBeingTracked);
    }

    private boolean isExistsUrl(String url) {
        return isCreatedUrlRepository.create(url) == 1L;
    }
}
