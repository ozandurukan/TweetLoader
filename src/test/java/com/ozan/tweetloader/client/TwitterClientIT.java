package com.ozan.tweetloader.client;

import com.ozan.tweetloader.domain.Tweet;
import com.ozan.tweetloader.exception.UserNotFoundException;
import com.ozan.tweetloader.service.TwitterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TwitterClientIT {

    @Autowired
    private TwitterClient twitterClient;

    private final static String TEST_USER_NAME = "ozanForTest";

    @Test
    public void twoTweets_getTweets_success() {
        List<Tweet> tweetList = twitterClient.getTweets(TEST_USER_NAME, null);
        assertThat(tweetList.size()).isEqualTo(3);

    }

    @Test(expected = UserNotFoundException.class)
    public void invalidUser_getTweets_thowException() {
        twitterClient.getTweets("veryLikelyInvalidUser", null);

    }

    @Test
    public void twoTweets_getTweetsSince_return1Tweet() {
        List<Tweet> tweetList = twitterClient.getTweets(TEST_USER_NAME, null);
        Tweet firstTweet = tweetList.get(1);
        List<Tweet> tweetsSince = twitterClient.getTweets(TEST_USER_NAME, firstTweet.getId());
        assertThat(tweetsSince.size()).isEqualTo(1);
        assertThat(tweetsSince.get(0).getText()).isEqualTo("I hate cold weather");
    }
}
