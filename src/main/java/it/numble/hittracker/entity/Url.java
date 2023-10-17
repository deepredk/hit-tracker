package it.numble.hittracker.entity;

import it.numble.hittracker.common.exception.InvalidUrlException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Url {

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String url;

    public Url(String url) {
        if (!isValid(url)) {
            throw new InvalidUrlException("형식이 맞지 않는 URL입니다");
        }
        this.url = url;
    }

    private boolean isValid(String url) {
        String regex = "^(https?)://[^\\s/$.?#].[^\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
