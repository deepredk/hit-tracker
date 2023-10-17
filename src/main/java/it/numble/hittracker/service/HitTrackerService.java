package it.numble.hittracker.service;

import it.numble.hittracker.controller.response.UrlHitInfoResponse;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
import it.numble.hittracker.exception.UrlAlreadyBeingTrackedException;
import it.numble.hittracker.exception.UrlNotBeingTrackedException;
import it.numble.hittracker.repository.DailyHitLogRepository;
import it.numble.hittracker.repository.HitRepository;
import it.numble.hittracker.repository.UrlRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    public void hit(String url) {
        hitRepository.hit(url);
    }

    public void tomorrow() {
        List<DailyHitLog> logs = dailyHitLogRepository.findAll();
        LocalDate today = logs.stream().map(DailyHitLog::getDate).max(Comparator.naturalOrder()).orElse(LocalDate.now()).plusDays(1);

        // 일간조회수 모두 0으로 초기화 및 저장
        List<Url> urls = urlRepository.findAll();
        for (Url url : urls) {
            DailyHitLog dailyHitLog = new DailyHitLog(today, hitRepository.getTodayHit(url.getUrl()));
            DailyHitLog savedDailyHitLog = dailyHitLogRepository.save(dailyHitLog);

            hitRepository.deleteTodayHit(url.getUrl());
            url.addLog(savedDailyHitLog);
            urlRepository.save(url);
        }

        // 7일이 지난 일간조회수는 삭제
        logs.stream().filter(log -> log.isSevenDaysAgo(today)).forEach(dailyHitLogRepository::delete);
    }

    public List<DailyHitLog> getStatistics(String url) {
        return urlRepository.findByUrl(url).orElseThrow().getDailyHitLogs();
    }

    private void track(String url) {
        Url urlBeingTracked = new Url(url);
        urlRepository.save(urlBeingTracked);
    }

    private boolean isExistsUrl(String url) {
        return urlRepository.existsByUrl(url);
    }
}
