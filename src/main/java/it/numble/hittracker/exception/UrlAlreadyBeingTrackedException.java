package it.numble.hittracker.exception;

public class UrlAlreadyBeingTrackedException extends BaseException {

    public UrlAlreadyBeingTrackedException() {
        super("이미 조회수 추적중인 URL입니다.");
    }
}
