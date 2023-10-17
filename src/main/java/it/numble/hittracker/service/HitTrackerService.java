package it.numble.hittracker.service;

import it.numble.hittracker.controller.response.UrlHitInfoResponse;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
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

    public UrlHitInfoResponse getHitInfo(String url) {
        if (!isExistsUrl(url)) {
            track(url);
        }

        return new UrlHitInfoResponse(
            url,
            hitRepository.getTodayHit(url),
            hitRepository.getTotalHit(url)
        );
    }

    @Async
    public void hit(String url) {
        hitRepository.hit(url);
    }

    public List<DailyHitLog> getStatistics(String url) {
        Url savedUrl = urlRepository.findByUrl(url).orElseGet(() -> urlRepository.save(new Url(url)));
        return dailyHitLogRepository.findAllByUrl(savedUrl);
    }

    public void tomorrow(Clock clock) {
        List<DailyHitLog> logs = dailyHitLogRepository.findAll();
        LocalDate today = LocalDate.now(clock).minusDays(1);

        // 일간조회수 모두 0으로 초기화 및 저장
        List<Url> urls = urlRepository.findAll();
        for (Url url : urls) {
            DailyHitLog dailyHitLog = new DailyHitLog(today, hitRepository.getTodayHit(url.getUrl()), url);
            dailyHitLogRepository.save(dailyHitLog);

            hitRepository.deleteTodayHit(url.getUrl());
        }

        // 7일이 지난 일간조회수는 삭제
        logs.stream().filter(log -> log.isSevenDaysAgo(today)).forEach(dailyHitLogRepository::delete);
    }

    private void track(String url) {
        Url urlBeingTracked = new Url(url);
        urlRepository.save(urlBeingTracked);
    }

    private boolean isExistsUrl(String url) {
        return urlRepository.existsByUrl(url);
    }
}
