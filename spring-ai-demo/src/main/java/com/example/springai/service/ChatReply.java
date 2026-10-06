package com.example.springai.service;

public record ChatReply(String provider, String model, String answer, String error, long latencyMs) {

    public static ChatReply ok(String provider, String model, String answer, long latencyMs) {
        return new ChatReply(provider, model, answer, null, latencyMs);
    }

    public static ChatReply failed(String provider, String model, String error, long latencyMs) {
        return new ChatReply(provider, model, null, error, latencyMs);
    }
}
