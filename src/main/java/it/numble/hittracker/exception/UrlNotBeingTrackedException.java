package it.numble.hittracker.exception;

import it.numble.hittracker.exception.BaseException;

public class UrlNotBeingTrackedException extends BaseException {

    public UrlNotBeingTrackedException() {
        super("아직 추적이 시작되지 않은 URL입니다.");
    }
}
