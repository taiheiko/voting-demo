package com.github.terigina.voting;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Application {

    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

    public static void main(String[] args) {
        port(PORT);
        staticFiles.location("/public");
        get("/vote/options", (req, res) -> {
            res.type("application/json");
            return "{\"options\": [\"red\", \"green\", \"blue\"]}";
        });
        post("/vote/:color", (req, res) -> {
            System.out.println("Vote for " + req.params("color"));
            return "";
        });
        get("/vote/results", (req, res) -> {
            res.type("application/json");
            return "{\"red\": 2, \"green\": 2, \"blue\": 1}";
        });
    }
}
