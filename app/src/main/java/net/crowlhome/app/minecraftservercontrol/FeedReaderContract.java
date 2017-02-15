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
        public static final int COLUMN_SERVER_QUERY_PORT = 25565;
        public static final int COLUMN_SERVER_RCON_PORT = 25575;
        public static final String COLUMN_SERVER_ICON = "# image location #";
        public static final int COLUMN_CURRENT_PLAYER_COUNT = 0;
        public static final int COLUMN_MAX_PLAYER_COUNT = 10;
        public static final String[] COLUMN_CURRENT_PLAYER_LIST = new String[1];
    }
}
