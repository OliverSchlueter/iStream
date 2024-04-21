package de.itbw18.istream.stream;

import de.itbw18.istream.streamconfig.StreamConfig;

public record Stream(String streamer, StreamConfig streamConfig, long liveSince, int amountViewers) {
}
