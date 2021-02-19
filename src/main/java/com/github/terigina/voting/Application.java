package com.github.terigina.voting;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {
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
