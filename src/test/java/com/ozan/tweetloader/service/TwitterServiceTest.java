package com.ozan.tweetloader.service;

import com.ozan.tweetloader.client.TwitterClient;
import com.ozan.tweetloader.domain.Tweet;
import com.ozan.tweetloader.exception.UserNotFoundException;
import com.ozan.tweetloader.repository.TweetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TwitterServiceTest {

    @InjectMocks
    TwitterService twitterService;

    @Mock
    TwitterClient twitterClient;

    @Mock
    TweetRepository tweetRepository;

    private final List<Tweet> tweets = prepareTweetListForRepo();

    private final static String DUMMY_USER_NAME = "dummy";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(tweetRepository.getAllTweets(DUMMY_USER_NAME)).thenReturn(prepareTweetListForRepo());
        Mockito.when(twitterClient.getTweets(DUMMY_USER_NAME, null)).thenReturn(prepareTweetListForClient());
    }

    private List<Tweet> prepareTweetListForRepo() {
        List<Tweet> tweets = new ArrayList<Tweet>();
        Tweet tweet1 = Tweet.builder().id(1L).userName(DUMMY_USER_NAME).text("tweet1").retweetCount(0).favoriteCount(0).build();
        Tweet tweet2 = Tweet.builder().id(2L).userName(DUMMY_USER_NAME).text("tweet2").retweetCount(10).favoriteCount(6).build();
        Tweet tweet3 = Tweet.builder().id(3L).userName(DUMMY_USER_NAME).text("tweet3").retweetCount(1).favoriteCount(15).build();
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);
        return tweets;
    }

    private List<Tweet> prepareTweetListForClient() {
        List<Tweet> tweets = new ArrayList<Tweet>();
        Tweet tweet1 = Tweet.builder().id(1L).userName(DUMMY_USER_NAME).text("tweet1").retweetCount(0).favoriteCount(0).build();
        Tweet tweet2 = Tweet.builder().id(2L).userName(DUMMY_USER_NAME).text("tweet2").retweetCount(10).favoriteCount(6).build();
        Tweet tweet3 = Tweet.builder().id(3L).userName(DUMMY_USER_NAME).text("tweet3").retweetCount(1).favoriteCount(15).build();
        Tweet tweet4 = Tweet.builder().id(4L).userName("dummy2").text("tweet4").retweetCount(1).favoriteCount(17).build();
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);
        tweets.add(tweet4);
        return tweets;
    }

    @Test
    public void oneMostFavoriteTweet_getMostFavoriteTweet_tweetReturned() {
        Optional<Tweet> optionalTweet = twitterService.getMostFavoriteTweet(DUMMY_USER_NAME);
        assertThat(optionalTweet.isPresent()).isTrue();
        assertThat(optionalTweet.get().getId()).isEqualTo(3L);
    }

    @Test
    public void lastTweetExists_loadTweets_tweetsLoadedWithSinceId() {
        Mockito.when(tweetRepository.getLastTweet(DUMMY_USER_NAME)).
                thenReturn(Optional.ofNullable(Tweet.builder().id(3L).text("tweet3").build()));

        List<Tweet> tweetList = twitterService.loadTweets(DUMMY_USER_NAME);
        Mockito.verify(twitterClient, times(1)).getTweets(DUMMY_USER_NAME, 3L);
    }

    @Test
    public void lastTweetDoesNotExist_loadTweets_tweetsLoadedWithNullSinceId() {
        Mockito.when(tweetRepository.getLastTweet(DUMMY_USER_NAME)).
                thenReturn(Optional.ofNullable(null));

        List<Tweet> tweetList = twitterService.loadTweets(DUMMY_USER_NAME);
        Mockito.verify(twitterClient, times(1)).getTweets(DUMMY_USER_NAME, null);
    }
}
