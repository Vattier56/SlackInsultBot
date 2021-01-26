package com.slack.bot.slackbot.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;

/*
* Model message for Evil Insult API
* */
@Getter @Setter
public class EvilInsultMessage implements Message {
    private final String bracketsRegex = "(\\[)|(\\])";

    private String insult;
    private String comment;
    private LANGUAGE LANG = LANGUAGE.EN;
    private String[] userList;

    public enum LANGUAGE {
        EN("en"), DE("de"), ES("es");

        private final String value;

        LANGUAGE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static String getLanguagesToRegex() {
            StringBuilder returnValue = new StringBuilder();

            for (LANGUAGE language : values()) {
                returnValue.append("(#").append(language.value).append(")|");
            }
            return returnValue.substring(0, returnValue.length() - 1);
        }
    }

    @Override
    public String getSource() {
        return "https://evilinsult.com";
    }

    @Override
    public String toString() {
        return getFormattedUserOutput() + insult;
    }

    public String toString(Boolean value) {
        String additionalInfo = comment != null ? "" : "Addition info : " + comment +  "\n";

        return getFormattedUserOutput() + insult + "\n" +
                "Language: " + getLANG().getValue() + "\n" +
                additionalInfo +
                "Source : " + getSource();
    }

    private String getFormattedUserOutput() {
        String users = (userList != null && userList.length >= 1) ? Arrays.toString(userList) + " " : "";
        users = users.replaceAll(bracketsRegex, "");
        return users;
    }
}
