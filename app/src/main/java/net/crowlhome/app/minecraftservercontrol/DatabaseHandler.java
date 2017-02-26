package net.crowlhome.app.minecraftservercontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ethen on 2/26/17.
 * Copyright Ethen Crowl
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "mainDatabase";

    // Server list table name
    private static final String TABLE_SERVER_LIST = "server";

    // Server List Column names
    private static final String KEY_ID = "server_id";
    private static final String KEY_SERVER_NAME = "server_name";
    private static final String KEY_SERVER_ADDRESS = "server_address";
    private static final String KEY_SERVER_PORT_RCON = "rcon_port";
    private static final String KEY_SERVER_PORT_QUERY = "query_port";
    private static final String KEY_SERVER_RCON_PASS = "rcon_password";
    private static final String KEY_SERVER_PLAYERS_CONNECTED = "connected_players";
    private static final String KEY_SERVER_PLAYERS_MAX = "max_players";
    private static final String KEY_SERVER_PING = "server_ping";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SERVER_LIST_TABLE = "CREATE TABLE " + TABLE_SERVER_LIST + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_SERVER_NAME + " TEXT," +
                KEY_SERVER_ADDRESS + " TEXT," +
                KEY_SERVER_PORT_RCON + " INTEGER," +
                KEY_SERVER_PORT_QUERY + " INTEGER," +
                KEY_SERVER_RCON_PASS + " TEXT," +
                KEY_SERVER_PLAYERS_CONNECTED + " INTEGER," +
                KEY_SERVER_PLAYERS_MAX + " INTEGER," +
                KEY_SERVER_PING + " INTEGER" + ")";
        db.execSQL(CREATE_SERVER_LIST_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVER_LIST);

        // Create the tables again
        onCreate(db);
    }

    public void addServer() {}
}
