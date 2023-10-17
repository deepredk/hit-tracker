package it.numble.hittracker.controller.response;

import lombok.Getter;

@Getter
public class UrlHitInfoResponse {
    private String url;
    private int todayHit;
    private int totalHit;

    public UrlHitInfoResponse(String url, int todayHit, int totalHit) {
        this.url = url;
        this.todayHit = todayHit;
        this.totalHit = totalHit;
    }
}
