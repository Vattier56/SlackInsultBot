package com.slack.bot.slackbot.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import javax.servlet.annotation.WebServlet;

/*
* Endpoint used by Slack to invoke commands/events
* */
@WebServlet("/slack/events")
public class SlackAppController extends SlackAppServlet {

    public SlackAppController(App app) {
        super(app);
    }

}
