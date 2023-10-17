package it.numble.hittracker.controller;

import it.numble.hittracker.common.response.DataResponseBody;
import it.numble.hittracker.common.response.BaseResponseBody;
import it.numble.hittracker.controller.dto.UrlHitInfoDto;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.service.HitTrackerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HitTrackerController {

    private final HitTrackerService hitTrackerService;

    @GetMapping("/count")
    public DataResponseBody<UrlHitInfoDto> getHitInfo(@RequestParam String url) {
        UrlHitInfoDto urlHitInfoDto = hitTrackerService.getHitInfo(url);
        return DataResponseBody.ok(urlHitInfoDto);
    }

    @PostMapping("/count")
    public BaseResponseBody hit(@RequestParam String url) {
        hitTrackerService.hit(url);
        return BaseResponseBody.ok();
    }

    @GetMapping("/statistics")
    public DataResponseBody<List<DailyHitLog>> statistics(@RequestParam String url) {
        return DataResponseBody.ok(hitTrackerService.getStatistics(url));
    }
}
