package de.itbw18.istream.stream.chat;

public record Message(String streamer, String sender, String content, long sent_at) {
}
