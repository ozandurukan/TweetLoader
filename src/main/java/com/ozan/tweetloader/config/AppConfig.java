package com.ozan.tweetloader.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({TwitterConfig.class, HazelcastConfig.class, WebSecurityConfig.class})
public class AppConfig {

}
