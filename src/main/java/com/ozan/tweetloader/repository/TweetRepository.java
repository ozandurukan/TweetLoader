package com.ozan.tweetloader.repository;

import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.ozan.tweetloader.domain.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TweetRepository {

    @Autowired
    private IMap<Long, Tweet> tweetStore;

    public void saveTweets(List<Tweet> tweets) {
        tweets.stream().forEach(tweet -> tweetStore.put(tweet.getId(), tweet));
    }

    public Optional<Tweet> getLastTweet(String userName) {
        Predicate namePredicate = Predicates.equal( "userName", userName );
        return tweetStore.values(namePredicate).stream()
                .max(Comparator.comparingLong(Tweet::getId));
    }

    public List<Tweet> getAllTweets(String userName) {
        Predicate namePredicate = Predicates.equal( "userName", userName );
        return tweetStore.values(namePredicate).stream().collect(Collectors.toList());
    }

}

