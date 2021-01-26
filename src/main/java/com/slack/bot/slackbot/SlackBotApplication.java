package com.slack.bot.slackbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SlackBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlackBotApplication.class, args);
    }

}
