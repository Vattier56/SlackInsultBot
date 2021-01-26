package com.slack.bot.slackbot.message.generator;

import com.slack.bot.slackbot.model.MatInsultMessage;
import com.slack.bot.slackbot.model.Message;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/*
* Message Generator for Mattbas API
* */
@Component
public class MatInsult extends MessageGenerator {
    private final String URL = "https://insult.mattbas.org/api/insult?";
    private final String WHO_PARAM = "who=";
    private final String PLURAL_PARAM = "&plural=on";

    private final Pattern users = Pattern.compile("(@[\\.\\w-]+[^\\s+])"),
                            showInfo = Pattern.compile("(-i)");

    public MatInsult() {
        super();
    }

    @Override
    public String generateMessage(String payload) {
        MatInsultMessage message = new MatInsultMessage();
        if (payload == null) return getMessage().toString();

        message.setUserList(
                users.matcher(payload)
                        .results()
                        .map(MatchResult::group)
                        .toArray(String[]::new)
        );

        if (message.getUserList().length >= 1) {
            message = (MatInsultMessage) getMessage(message);
        } else message = (MatInsultMessage) getMessage();

        if (showInfo.matcher(payload.toLowerCase()).find()) {
            return message.toString(true);
        } else return message.toString();
    }

    @Override
    Message getMessage() {
        try {
            MatInsultMessage message = new MatInsultMessage();
            message.setInsult(
                    fetchMessageAsText(new MatInsultMessage(), new URI(URL))
            );
            return message;
        } catch (Exception exception) {
            exception.printStackTrace();

            MatInsultMessage exceptionMessage = new MatInsultMessage();
            exceptionMessage.setInsult("Even this program could not find a suitable insult");

            return exceptionMessage;
        }
    }

    Message getMessage(MatInsultMessage message) {
        try {
            if (message.getUserList().length > 1) {
                message.setInsult(fetchMessageAsText(message, new URI(
                        URL + WHO_PARAM + message.getUsersAsString() + PLURAL_PARAM
                )));
            } else {
                message.setInsult(fetchMessageAsText(message, new URI(
                        URL + WHO_PARAM + message.getUsersAsString()
                )));
            }
            return message;
        } catch (Exception exception) {
            exception.printStackTrace();

            message.setInsult("Even this program could not find a suitable insult");
            return message;
        }
    }
}
