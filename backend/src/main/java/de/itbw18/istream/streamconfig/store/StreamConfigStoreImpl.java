package de.itbw18.istream.streamconfig.store;

import de.itbw18.istream.streamconfig.StreamConfig;
import de.itbw18.istream.streamconfig.store.database.StreamConfigDatabase;

public class StreamConfigStoreImpl implements StreamConfigStore {

    private final StreamConfigDatabase streamConfigDatabase;

    public StreamConfigStoreImpl(StreamConfigDatabase streamConfigDatabase) {
        this.streamConfigDatabase = streamConfigDatabase;
    }

    @Override
    public void setup() {
        streamConfigDatabase.setup();
    }

    @Override
    public void createStreamConfig(StreamConfig streamConfig) {
        streamConfigDatabase.insertStreamConfig(streamConfig);
    }

    @Override
    public StreamConfig getStreamConfig(String userId) {
        return streamConfigDatabase.getStreamConfig(userId);
    }

    @Override
    public void updateStreamConfig(StreamConfig streamConfig) {
        streamConfigDatabase.updateStreamConfig(streamConfig);
    }

    @Override
    public void deleteStreamConfig(String userId) {
        streamConfigDatabase.deleteStreamConfig(userId);
    }
}
