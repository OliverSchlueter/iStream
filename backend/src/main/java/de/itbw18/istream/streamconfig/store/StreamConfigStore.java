package de.itbw18.istream.streamconfig.store;

import de.itbw18.istream.streamconfig.StreamConfig;

public interface StreamConfigStore {

    void setup();

    void createStreamConfig(StreamConfig streamConfig);

    StreamConfig getStreamConfig(String userId);

    void updateStreamConfig(StreamConfig streamConfig);

    void deleteStreamConfig(String userId);
}
