package com.slack.bot.slackbot.message.generator;

import com.slack.bot.slackbot.model.EvilInsultMessage;
import com.slack.bot.slackbot.model.Message;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Message Generator for EvilInsult API
 * */
@Component
public class EvilInsult extends MessageGenerator {
    private final String URL = "https://evilinsult.com/generate_insult.php?";
    private final String JSON_TYPE_PARAM = "&type=json";
    private final String LANG_PARAM = "lang=";

    private final Pattern users = Pattern.compile("(@[\\.\\w-]+[^\\s+])"),
                            showInfo = Pattern.compile("(-i)"),
                            language = Pattern.compile(
                                    EvilInsultMessage.LANGUAGE.getLanguagesToRegex()
                            );

    public EvilInsult() {
        super();
    }


    @Override
    public String generateMessage(String payload) {
        EvilInsultMessage message;
        if (payload == null) return getMessage().toString();

        Matcher matcher = language.matcher(payload.toLowerCase());

        if (matcher.find()) {
            message = (EvilInsultMessage) getMessage(
                    EvilInsultMessage.LANGUAGE.valueOf(
                            matcher.group().substring(1).toUpperCase()
                    )
            );
        } else message = (EvilInsultMessage) getMessage();

        message.setUserList(
                users.matcher(payload)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new)
        );

        if (showInfo.matcher(payload.toLowerCase()).find()) {
            return message.toString(true);
        } else return message.toString();
    }


    Message getMessage() {
        try {
            return fetchMessage(new EvilInsultMessage(), new URI(
                    URL + LANG_PARAM + EvilInsultMessage.LANGUAGE.EN.getValue() + JSON_TYPE_PARAM
            ));
        } catch (Exception exception) {

            EvilInsultMessage exceptionMessage = new EvilInsultMessage();
            exceptionMessage.setInsult("Even this program could not find a suitable insult");

            return exceptionMessage;
        }
    }

    Message getMessage(EvilInsultMessage.LANGUAGE language) {
        try {
            return fetchMessage(new EvilInsultMessage(), new URI(
                    URL + LANG_PARAM + language.getValue() + JSON_TYPE_PARAM
            ));
        } catch (Exception exception) {

            EvilInsultMessage exceptionMessage = new EvilInsultMessage();
            exceptionMessage.setInsult("Even this program could not find a suitable insult");

            return exceptionMessage;
        }
    }
}
