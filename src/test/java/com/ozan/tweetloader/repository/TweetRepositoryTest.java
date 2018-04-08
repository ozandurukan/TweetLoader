package com.ozan.tweetloader.repository;


import com.ozan.tweetloader.domain.Tweet;
import com.ozan.tweetloader.repository.TweetRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TweetRepositoryTest {

    private static final String DUMMY_USER_NAME = "dummy";

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    Map<Long, Tweet> tweetMap;

    @Before
    public void setup() {
        tweetMap.clear();
        List<Tweet> tweets = new ArrayList<Tweet>();
        Tweet tweet1 = Tweet.builder().id(1L).userName(DUMMY_USER_NAME).text("tweet1").retweetCount(0).favoriteCount(0).build();
        Tweet tweet2 = Tweet.builder().id(2L).userName(DUMMY_USER_NAME).text("tweet2").retweetCount(10).favoriteCount(6).build();
        Tweet tweet3 = Tweet.builder().id(3L).userName(DUMMY_USER_NAME).text("tweet3").retweetCount(1).favoriteCount(15).build();
        Tweet tweet4 = Tweet.builder().id(4L).userName("dummy2").text("tweet4").retweetCount(10).favoriteCount(15).build();
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);
        tweets.add(tweet4);

        tweetRepository.saveTweets(tweets);
        assertThat(tweetMap.size()).isEqualTo(4);
    }

    @Test
    public void threeTweetsForUser_getLastTweet_lastTweetReturned() {
        Optional<Tweet> optTweet = tweetRepository.getLastTweet(DUMMY_USER_NAME);
        assertThat(optTweet.isPresent()).isTrue();
        assertThat(optTweet.get().getId()).isEqualTo(3L);
    }

    @Test
    public void noTweetsForUser_getLastTweet_emptyTweetReturned() {
        Optional<Tweet> optTweet = tweetRepository.getLastTweet("dummy3");
        assertThat(optTweet.isPresent()).isFalse();
    }

    @Test
    public void noTweetsForUser_getAllTweets_emptyListReturned() {
        List<Tweet> tweets = tweetRepository.getAllTweets("dummy3");
        assertThat(tweets.isEmpty()).isTrue();
    }

    @Test
    public void threeTweetsForUser_getAllTweets_correctListReturned() {
        List<Tweet> tweets = tweetRepository.getAllTweets(DUMMY_USER_NAME);
        assertThat(tweets.size()).isEqualTo(3);
    }

    @After
    public void tearDown() {
        tweetMap.clear();
    }
}
