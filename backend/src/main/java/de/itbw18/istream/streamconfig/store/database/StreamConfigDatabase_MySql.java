package de.itbw18.istream.streamconfig.store.database;

import de.itbw18.istream.cmd.main.Backend;
import de.itbw18.istream.helpers.database.SQLConnector;
import de.itbw18.istream.streamconfig.StreamConfig;

public class StreamConfigDatabase_MySql implements StreamConfigDatabase {

    private final SQLConnector db;

    public StreamConfigDatabase_MySql(SQLConnector db) {
        this.db = db;
    }

    @Override
    public void setup() {
        if (!db.isConnected()) {
            throw new IllegalStateException("Database is not connected");
        }
    }

    @Override
    public void insertStreamConfig(StreamConfig streamConfig) {
        try {
            db.getConnection().createStatement().execute("INSERT INTO stream_configs (user_id, title, description, category) VALUES ('" + streamConfig.userId() + "', '" + streamConfig.title() + "', '" + streamConfig.description() + "', '" + streamConfig.category().name() + "')");
        } catch (Exception e) {
            Backend.LOGGER.error("Failed to insert stream config", e);
        }
    }

    @Override
    public StreamConfig getStreamConfig(String userId) {
        try {
            var result = db.getConnection().createStatement().executeQuery("SELECT * FROM stream_configs WHERE user_id = '" + userId + "'");
            if (result.next()) {
                return new StreamConfig(
                        result.getString("user_id"),
                        result.getString("title"),
                        result.getString("description"),
                        StreamConfig.Category.valueOf(result.getString("category"))
                );
            }
        } catch (Exception e) {
            Backend.LOGGER.error("Failed to get stream config", e);
        }
        return null;
    }

    @Override
    public void updateStreamConfig(StreamConfig streamConfig) {
        try {
            db.getConnection().createStatement().execute("UPDATE stream_configs SET title = '" + streamConfig.title() + "', description = '" + streamConfig.description() + "', category = '" + streamConfig.category().name() + "' WHERE user_id = '" + streamConfig.userId() + "'");
        } catch (Exception e) {
            Backend.LOGGER.error("Failed to update stream config", e);
        }
    }

    @Override
    public void deleteStreamConfig(String userId) {
        try {
            db.getConnection().createStatement().execute("DELETE FROM stream_configs WHERE user_id = '" + userId + "'");
        } catch (Exception e) {
            Backend.LOGGER.error("Failed to delete stream config", e);
        }
    }
}
