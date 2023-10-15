package it.numble.hittracker.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UrlHitInfoResponse {
    private String url;
    private int dailyHit;
    private int totalHit;

    public UrlHitInfoResponse(String url, int dailyHit, int totalHit) {
        this.url = url;
        this.dailyHit = dailyHit;
        this.totalHit = totalHit;
    }
}
