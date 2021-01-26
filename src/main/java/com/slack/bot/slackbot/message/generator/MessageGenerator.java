package com.slack.bot.slackbot.message.generator;

import com.slack.bot.slackbot.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.URI;

/*
* Base class for Message Generators
* */
public abstract class MessageGenerator {

    public abstract String generateMessage(String payload);
    abstract Message getMessage();

    Message fetchMessage(Message response, URI uri) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<? extends Message> responseEntity =
                restTemplate.getForEntity(uri, response.getClass());

        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            System.err.println(response.getClass().getSimpleName() + " API Exception with code :" + responseEntity.getStatusCodeValue());
            return null;
        }

        return responseEntity.getBody();
    }

    String fetchMessageAsText(Message response, URI uri) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(uri, String.class);

        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            System.err.println(response.getClass().getSimpleName() + " API Exception with code :" + responseEntity.getStatusCodeValue());
            return null;
        }

        return responseEntity.getBody();
    }
}
