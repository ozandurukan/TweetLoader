package com.ozan.tweetloader.service;

import com.ozan.tweetloader.client.TwitterClient;
import com.ozan.tweetloader.domain.Tweet;
import com.ozan.tweetloader.exception.UserNotFoundException;
import com.ozan.tweetloader.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class TwitterService {

    @Autowired
    private TwitterClient twitterClient;

    @Autowired
    private TweetRepository tweetRepository;

    public List<Tweet> getTweets(String userName) {
        return twitterClient.getTweets(userName, null);
    }

    public List<Tweet> getTweetsSince(String userName, Long sinceTweetId) {
        return twitterClient.getTweets(userName, sinceTweetId);
    }

    public List<Tweet> loadTweets(String userName) {
        Optional<Tweet> lastTweet = tweetRepository.getLastTweet(userName);
        List<Tweet> tweetList;
        if (lastTweet.isPresent()) {
            tweetList = this.getTweetsSince(userName, lastTweet.get().getId());
        } else {
            tweetList = this.getTweets(userName);
        }
        tweetRepository.saveTweets(tweetList);
        return tweetList;

    }

    public List<Tweet> getAllTweets(String userName) {
        return tweetRepository.getAllTweets(userName);
    }

    public Optional<Tweet> getMostFavoriteTweet(String userName) {
        return getAllTweets(userName).stream().max(Comparator.comparingInt(Tweet::getFavoriteCount));
    }
}
