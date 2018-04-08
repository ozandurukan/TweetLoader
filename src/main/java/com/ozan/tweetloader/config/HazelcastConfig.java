package com.ozan.tweetloader.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.ozan.tweetloader.domain.Tweet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    IMap<Long, Tweet> createHazelcastDataGrid() {
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
        return hzInstance.getMap("tweets");
    }
}
