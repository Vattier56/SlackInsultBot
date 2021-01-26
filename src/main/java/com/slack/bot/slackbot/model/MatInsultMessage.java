package com.slack.bot.slackbot.model;

import lombok.Getter;
import lombok.Setter;

/*
 * Model message for Evil Insult API
 * */
@Getter @Setter
public class MatInsultMessage implements Message {
    private String insult;
    private String[] userList;

    @Override
    public String getSource() {
        return "https://insult.mattbas.org/";
    }

    public String getUsersAsString() {
        StringBuilder returnValue = new StringBuilder();

        for (String user : userList) {
            returnValue.append(user).append("+");
        }

        return returnValue.substring(0, returnValue.length() - 1);
    }

    @Override
    public String toString() {
        return insult;
    }

    public String toString(Boolean value) {
        return  insult + "\n" +
                "Source : " + getSource();
    }
}
