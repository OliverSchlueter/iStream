package de.itbw18.istream.stream.store;

import de.itbw18.istream.stream.Stream;
import de.itbw18.istream.user.User;

import java.util.List;

public interface StreamStore {

    void startStream(User streamer);

    void stopStream(User streamer);

    void updateViewers(String streamerId, int viewers);

    Stream getStream(User streamer);

    List<Stream> getAllStreams();

}
