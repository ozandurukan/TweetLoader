package com.ozan.tweetloader.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class Tweet implements Serializable{
    private String userName;

    @JsonProperty("user")
    private void unpackNameFromNestedObject(JsonNode user) {
        userName = user.get("screen_name").asText();
    }
    private String text;
    private Long id;
    @JsonProperty("retweet_count")
    private int retweetCount;
    @JsonProperty("favorite_count")
    private int favoriteCount;

}
