package com.ozan.tweetloader.controller;

import com.ozan.tweetloader.domain.Tweet;
import com.ozan.tweetloader.exception.UserNotFoundException;
import com.ozan.tweetloader.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TweetLoaderController {

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value="/loadTweets", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Tweet> loadTweets(@RequestParam(value = "userName", required = true) String userName) {
        return twitterService.loadTweets(userName);
    }

    @RequestMapping(value="/getAllTweets", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Tweet> getAllTweets(@RequestParam(value = "userName", required = true) String userName){
        return twitterService.getAllTweets(userName);
    }

    @RequestMapping(value="/getMostFavoriteTweet", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Tweet getMostFavoriteTweet(@RequestParam(value = "userName", required = true) String userName) {
        Optional<Tweet> optionalTweet = twitterService.getMostFavoriteTweet(userName);
        if (optionalTweet.isPresent()) {
            return optionalTweet.get();
        } else {
            throw new UserNotFoundException(userName + " not exist in database");
        }
    }
}
