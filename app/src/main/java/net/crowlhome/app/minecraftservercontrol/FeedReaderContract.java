package net.crowlhome.app.minecraftservercontrol;

import android.provider.BaseColumns;

/**
 * Created by ethen on 2/15/17.
 * Copyright Ethen Crowl
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_SERVER_NAME = "name";
        public static final String COLUMN_SERVER_MOTD = "motd";
        public static final String COLUMN_SERVER_HOSTNAME = "hostname";
        public static final String COLUMN_SERVER_QUERY_PORT = "query_port";
        public static final String COLUMN_SERVER_RCON_PORT = "rcon_port";
        public static final String COLUMN_SERVER_ICON = "server_icon";
        public static final String COLUMN_CURRENT_PLAYER_COUNT = "current_player_count";
        public static final String COLUMN_MAX_PLAYER_COUNT = "max_player_count";
        public static final String COLUMN_CURRENT_PLAYER_LIST = "current_player_list";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY NOT NULL," +
                    FeedEntry.COLUMN_SERVER_NAME + " TEXT NOT NULL," +
                    FeedEntry.COLUMN_SERVER_MOTD + " TEXT," +
                    FeedEntry.COLUMN_SERVER_HOSTNAME + " TEXT NOT NULL," +
                    FeedEntry.COLUMN_SERVER_QUERY_PORT + " INTEGER NOT NULL," +
                    FeedEntry.COLUMN_SERVER_RCON_PORT + " INTEGER NOT NULL," +
                    FeedEntry.COLUMN_SERVER_ICON + " TEXT," +
                    FeedEntry.COLUMN_CURRENT_PLAYER_COUNT + " INTEGER," +
                    FeedEntry.COLUMN_MAX_PLAYER_COUNT + " INTEGER," +
                    FeedEntry.COLUMN_CURRENT_PLAYER_LIST + " BLOB)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
