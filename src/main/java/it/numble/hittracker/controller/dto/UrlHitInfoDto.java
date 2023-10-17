package it.numble.hittracker.controller.dto;

import lombok.Getter;

@Getter
public class UrlHitInfoDto {

    private String url;
    private int todayHit;
    private int totalHit;

    public UrlHitInfoDto(String url, int todayHit, int totalHit) {
        this.url = url;
        this.todayHit = todayHit;
        this.totalHit = totalHit;
    }
}
