package com.github.taya_y.voting;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {
        get("/", (req, res) -> "Voting App");
    }
}
