package de.itbw18.istream.streamconfig.store.database;

import de.itbw18.istream.streamconfig.StreamConfig;

public interface StreamConfigDatabase {

    void setup();

    void insertStreamConfig(StreamConfig streamConfig);

    StreamConfig getStreamConfig(String userId);

    void updateStreamConfig(StreamConfig streamConfig);

    void deleteStreamConfig(String userId);
}
