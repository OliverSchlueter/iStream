package de.itbw18.istream.streamconfig.store.database;

import de.itbw18.istream.streamconfig.StreamConfig;

import java.util.ArrayList;
import java.util.List;

public class StreamConfigDatabase_Fake implements StreamConfigDatabase {

    private List<StreamConfig> streamConfigs;

    @Override
    public void setup() {
        streamConfigs = new ArrayList<>();
    }

    @Override
    public void insertStreamConfig(StreamConfig streamConfig) {
        streamConfigs.add(streamConfig);
    }

    @Override
    public StreamConfig getStreamConfig(String userId) {
        for (StreamConfig streamConfig : streamConfigs) {
            if (streamConfig.userId().equals(userId)) {
                return streamConfig;
            }
        }

        return null;
    }

    @Override
    public void updateStreamConfig(StreamConfig streamConfig) {
        streamConfigs.removeIf(sc -> sc.userId().equals(streamConfig.userId()));
        streamConfigs.add(streamConfig);
    }

    @Override
    public void deleteStreamConfig(String userId) {
        streamConfigs.removeIf(sc -> sc.userId().equals(userId));
    }
}
