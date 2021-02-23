package com.github.terigina.voting;

import com.github.terigina.voting.service.VotingService;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Session;

import java.util.UUID;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.stop;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    private static final VotingService votingService = new VotingService();
    private static final DataToJsonConverter dataToJsonConverter = new DataToJsonConverter();

    static {
//        System.setProperty("org.slf4j.simpleLogger.log.org.eclipse.jetty.server.session", "debug");
    }

    public static void main(String[] args) {
        port(PORT);
        staticFiles.location("/public");
        before(initializeSession());
        get("/vote/options", (req, res) -> {
            res.type("application/json");
            return dataToJsonConverter.convert(votingService.getOptions(req.session().attribute("user_id")));
        });
        post("/vote/:color", (req, res) -> {
            if (!votingService.vote(req.session().attribute("user_id"), req.params("color"))) {
                res.status(HttpStatus.BAD_REQUEST_400);
            }
            return "";
        });
        get("/vote/results", (req, res) -> {
            res.type("application/json");
            return dataToJsonConverter.convert(votingService.getResults());
        });

        addShutdownHook();
    }

    private static void addShutdownHook() {
        Runnable hook = () -> {
            LOGGER.warn("Stopping the App...");
            try {
                votingService.close();
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }

    private static Filter initializeSession() {
        return (req, res) -> {
            Session session = req.session(true);
            if (session.isNew()) {
                session.attribute("user_id", UUID.randomUUID());
            }
        };
    }
}
