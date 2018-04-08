package com.ozan.tweetloader.controller;

import com.ozan.tweetloader.domain.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetLoaderControllerE2E {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String createUrl() {
        return "http://localhost:"+port;
    }

    @Test
    public void existingUser_loadTweets_tweetsLoaded() {

        ResponseEntity<List<Tweet>> response = restTemplate
                .exchange(createUrl()+"/loadTweets?userName=ozanForTest",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Tweet>>() {
                });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().size()).isEqualTo(3);
    }

    @Test
    public void userDoesNotExist_loadTweets_404Returned() {

        ResponseEntity response = restTemplate
                .getForEntity(createUrl()+"/loadTweets?userName=ozanForTest121212", String.class );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void existingUser_getAllTweets_tweetsReturned() {

        ResponseEntity<List<Tweet>> response = restTemplate
                .exchange(createUrl()+"/getAllTweets?userName=ozanForTest",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Tweet>>() {
                        });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void userExistsInDB_getMostFavoriteTweet_tweetReturned() {

        //load tweets for this user first
        ResponseEntity<List<Tweet>> response = restTemplate
                .exchange(createUrl()+"/loadTweets?userName=ozandurukan",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Tweet>>() {
                        });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        ResponseEntity responseForFavorite = restTemplate
                .getForEntity(createUrl()+"/getMostFavoriteTweet?userName=ozandurukan", String.class);
        assertThat(responseForFavorite.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
