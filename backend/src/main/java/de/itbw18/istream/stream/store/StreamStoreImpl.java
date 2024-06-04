package de.itbw18.istream.stream.store;

import de.itbw18.istream.stream.Stream;
import de.itbw18.istream.streamconfig.StreamConfig;
import de.itbw18.istream.user.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StreamStoreImpl implements StreamStore {

    private final Map<String, Stream> liveStreams;

    public StreamStoreImpl() {
        this.liveStreams = new ConcurrentHashMap<>();
    }

    @Override
    public void startStream(User streamer) {
        // TODO: get stream config from user
        StreamConfig config = null;

        Stream stream = new Stream(streamer.id(), config, System.currentTimeMillis(), 0);
        liveStreams.put(streamer.id(), stream);
    }

    @Override
    public void stopStream(User streamer) {
        liveStreams.remove(streamer.id());
    }

    @Override
    public void updateViewers(String streamerId, int viewers) {
        Stream stream = liveStreams.get(streamerId);
        if (stream != null) {
            return;
        }

        if (viewers < 0) {
            throw new IllegalArgumentException("Viewers must be greater than or equal to 0");
        }

        Stream updatedStream = new Stream(streamerId, stream.streamConfig(), stream.liveSince(), viewers);
        liveStreams.put(streamerId, updatedStream);
    }

    @Override
    public Stream getStream(User streamer) {
        return liveStreams.get(streamer.id());
    }

    @Override
    public List<Stream> getAllStreams() {
        return List.copyOf(liveStreams.values());
    }
}
