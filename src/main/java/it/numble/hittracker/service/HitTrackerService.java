package it.numble.hittracker.service;

import it.numble.hittracker.controller.response.UrlHitInfoResponse;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
import it.numble.hittracker.exception.UrlAlreadyBeingTrackedException;
import it.numble.hittracker.exception.UrlNotBeingTrackedException;
import it.numble.hittracker.repository.DailyHitLogRepository;
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

    public void track(String url) {
        if (isExistsUrl(url)) {
            throw new UrlAlreadyBeingTrackedException();
        }

        UrlBeingTracked urlBeingTracked = new UrlBeingTracked(url);
        urlRepository.save(urlBeingTracked);
    }

    private boolean isExistsUrl(String url) {
        return urlRepository.existsByUrl(url);
    }

    public UrlHitInfoResponse getHitInfo(String url) {
        UrlBeingTracked urlBeingTracked = urlRepository.findByUrl(url)
            .orElseThrow(UrlNotBeingTrackedException::new);

        return new UrlHitInfoResponse(
            url,
            urlBeingTracked.getDailyHit(),
            urlBeingTracked.getTotalHit()
        );
    }

    public void hit(String url) {
        UrlBeingTracked urlBeingTracked = urlRepository.findByUrl(url)
            .orElseThrow(UrlNotBeingTrackedException::new);

        urlBeingTracked.hit();
        urlRepository.save(urlBeingTracked);
    }

    public void tomorrow() {
        List<DailyHitLog> logs = dailyHitLogRepository.findAll();
        LocalDate today = logs.stream().map(DailyHitLog::getDate).max(Comparator.naturalOrder()).orElse(LocalDate.now()).plusDays(1);

        // 일간조회수 모두 0으로 초기화 및 저장
        List<UrlBeingTracked> urls = urlRepository.findAll();
        for (UrlBeingTracked url : urls) {
            DailyHitLog dailyHitLog = new DailyHitLog(today, url.getDailyHit());
            DailyHitLog savedDailyHitLog = dailyHitLogRepository.save(dailyHitLog);

            url.addLog(savedDailyHitLog);
            url.initiailizeDailyHit();
            urlRepository.save(url);
        }

        // 7일이 지난 일간조회수는 삭제
        logs.stream().filter(log -> log.isSevenDaysAgo(today)).forEach(dailyHitLogRepository::delete);
    }

    public List<DailyHitLog> getStatistics(String url) {
        return urlRepository.findByUrl(url).orElseThrow().getDailyHitLogs();
    }
}
