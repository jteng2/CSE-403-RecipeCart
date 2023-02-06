/* (C)2023 */
package com.gradle.tutorial;

import static spark.Spark.*;

public class SparkExample {
    // Spark uses a default port of 4567
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
