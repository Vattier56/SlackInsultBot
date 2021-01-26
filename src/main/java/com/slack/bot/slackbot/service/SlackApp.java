package com.slack.bot.slackbot.service;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.event.MemberLeftChannelEvent;
import com.slack.bot.slackbot.message.generator.EvilInsult;
import com.slack.bot.slackbot.message.generator.MatInsult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.view.Views.view;

/*
* Class handling slack events and commands
* Requires Environmental Variable SLACK_BOT_TOKEN & SLACK_SIGNING_SECRET
 * */
@Configuration
public class SlackApp  {
    private final EvilInsult evilInsult;
    private final MatInsult matInsult;

    public SlackApp(EvilInsult evilInsult, MatInsult matInsult) {
        this.evilInsult = evilInsult;
        this.matInsult = matInsult;
    }

    @Bean
    public App initSlackApp() {
        App app = new App();
        app.command("/hello", (req, ctx) -> {
            return ctx.ack("Hello " + req.getPayload().getUserName() + "! :wave:\nPsst. You want to insult someone ?:sunglasses:");
        });

        app.command("/insult", (req, ctx) -> {
            return ctx.ack(res -> {
                try {
                    return res.responseType("in_channel").text(evilInsult.generateMessage(req.getPayload().getText()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return res.responseType("in_channel").text("Something went wrong :(\n Please restart me");
                }
            });
        });

        app.command("/offend", (req, ctx) -> {
            return ctx.ack(res -> {
                try {
                    return res.responseType("in_channel").text(matInsult.generateMessage(req.getPayload().getText()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return res.responseType("in_channel").text("Something went wrong :(\n Please restart me");
                }
            });
        });


//        Requires Oauth Access Token with the required scopes
        app.event(MemberLeftChannelEvent.class, (req, ctx) -> {
            MemberLeftChannelEvent event = req.getEvent();

            ChatPostMessageResponse message = ctx.client().chatPostMessage(r -> r
                    .channel(event.getChannel())
                    .threadTs(event.getType())
                    .text("What's he so upset about?"));
            if (!message.isOk()) {
                ctx.logger.error("chat.postMessage failed: {}", message.getError());
            }
            return ctx.ack();
        });

        app.event(AppHomeOpenedEvent.class, (req, ctx) -> {
            var logger = ctx.logger;
            var userId = req.getEvent().getUser();
            try {
                var modalView = view(v -> v
                        .type("home")
                        .blocks(asBlocks(
                                header(h -> h.text(plainText(mt ->
                                        mt.text("Insult Slack Bot ")))),
                                header(h -> h.text(plainText(mt ->
                                        mt.text("Designed with love to insult people :smiling_face_with_3_hearts: ")))),
                                divider(),
                                section(s -> s.text(markdownText(mt ->
                                        mt.text("*Welcome home, <@" + userId + "> :house:*")))),
                                divider(),

                                section(s -> s.text(markdownText(mt ->
                                        mt.text(
                                                "Available commends :\n\n" +
                                                "/hello \n" +
                                                "/insult \n \t@User (which user(s) to offend)\n \t#Language (EN, DE, ES)" +
                                                        "\n \t -i (show additional information about message) \n" +
                                                "/offend (specially designed to insult many people)\n \t@User (which user(s) to offend)" +
                                                "\n \t -i (show additional information about message) \n"
                                        )))),
                                divider(),

                                divider(),
                                header(h -> h.text(plainText(mt ->
                                        mt.text("Created by\n\n Piotr Piasecki"))))

                        ))
                );
                var result = ctx.client().viewsPublish(r -> r
                        .token(System.getenv("SLACK_BOT_TOKEN"))
                        .userId(userId)
                        .view(modalView)
                );
            } catch (IOException | SlackApiException e) {
                logger.error("error: {}", e.getMessage(), e);
            }
            return ctx.ack();
        });
        return app;
    }
}
