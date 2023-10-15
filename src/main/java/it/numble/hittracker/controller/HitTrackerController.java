package it.numble.hittracker.controller;

import it.numble.hittracker.controller.response.Response;
import it.numble.hittracker.controller.response.UrlHitInfoResponse;
import it.numble.hittracker.controller.response.dto.Empty;
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

    @PostMapping("/track")
    public Response<Empty> track(@RequestParam String url) {
        hitTrackerService.track(url);
        return Response.ok();
    }

    @GetMapping("/count")
    public Response<UrlHitInfoResponse> getHitInfo(@RequestParam String url) {
        UrlHitInfoResponse urlHitInfoResponse = hitTrackerService.getHitInfo(url);
        return Response.okWithDetail(urlHitInfoResponse);
    }

    @PostMapping("/count")
    public Response<Empty> hit(@RequestParam String url) {
        hitTrackerService.hit(url);
        return Response.ok();
    }

    @PostMapping("/tomorrow")
    public Response<Empty> tomorrow() {
        hitTrackerService.tomorrow();
        return Response.ok();
    }

    @GetMapping("/statistics")
    public Response<List<DailyHitLog>> statistics(@RequestParam String url) {
        return Response.okWithDetail(hitTrackerService.getStatistics(url));
    }
}
