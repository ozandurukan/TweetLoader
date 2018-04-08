package com.ozan.tweetloader.client;

import com.ozan.tweetloader.domain.Tweet;
import com.ozan.tweetloader.exception.TweetLoaderException;
import com.ozan.tweetloader.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Component
@Slf4j
public class TwitterClient {

    private static final String SCREEN_NAME_PARAM = "screen_name";
    private static final String SINCE_ID_PARAM = "since_id";
    private static final String USER_TIMELINE = "user_timeline.json";

    @Autowired
    OAuth2RestOperations restTemplate;


    @Value("${twitter.url}")
    private String url;


    public List<Tweet> getTweets(String userName, Long sinceTweetId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + USER_TIMELINE)
                .queryParam(SCREEN_NAME_PARAM, userName);
        if (sinceTweetId != null) {
            builder.queryParam(SINCE_ID_PARAM, sinceTweetId);
        }
        ResponseEntity<List<Tweet>> response;
        try {
            response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Tweet>>() {
                    });
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                String message = userName + " user not exist on Twitter";
                System.out.println(message);
                throw new UserNotFoundException(message);
            } else {
                throw new TweetLoaderException(e.getMessage());
            }
        }

        return response.getBody();
    }


}
