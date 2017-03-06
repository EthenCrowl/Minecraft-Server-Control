package net.crowlhome.app.minecraftservercontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.session.PlaybackState;

import java.util.ArrayList;
import java.util.List;

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
    private static final String KEY_SERVER_PORT_QUERY = "query_port";
    private static final String KEY_SERVER_PORT_RCON = "rcon_port";
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
                KEY_SERVER_PORT_QUERY + " INTEGER," +
                KEY_SERVER_PORT_RCON + " INTEGER," +
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

    public void addServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_NAME, server.get_serverName());
        values.put(KEY_SERVER_ADDRESS, server.get_serverAddress());
        values.put(KEY_SERVER_PORT_QUERY, server.get_queryPort());
        values.put(KEY_SERVER_PORT_RCON, server.get_rconPort());
        values.put(KEY_SERVER_RCON_PASS, server.get_rconPass());

        // Insert Row
        db.insert(TABLE_SERVER_LIST, null, values);
        db.close(); // Close database connection
    }

    public Server getServer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERVER_LIST, new String[] { KEY_ID,
        KEY_SERVER_NAME, KEY_SERVER_ADDRESS, KEY_SERVER_PORT_QUERY, KEY_SERVER_PORT_RCON,
        KEY_SERVER_RCON_PASS, KEY_SERVER_PLAYERS_CONNECTED, KEY_SERVER_PLAYERS_MAX, KEY_SERVER_PING}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Server server = new Server(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6),
                cursor.getInt(7), cursor.getInt(8));

        return server;
    }

    public List<Server> getAllServers() {
        List<Server> serverList = new ArrayList<Server>();
        // Selet All Query
        String selectQuery = "SELECT * FROM " + TABLE_SERVER_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add them to the list
        if (cursor.moveToFirst()) {
            do {
                Server server = new Server(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7), cursor.getInt(8));
                serverList.add(server);
            } while (cursor.moveToNext());
        }

        return serverList;
    }

    public int updateServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_NAME, server.get_serverName());
        values.put(KEY_SERVER_ADDRESS, server.get_serverAddress());
        values.put(KEY_SERVER_PORT_QUERY, server.get_queryPort());
        values.put(KEY_SERVER_PORT_RCON, server.get_rconPort());
        values.put(KEY_SERVER_RCON_PASS, server.get_rconPass());
        values.put(KEY_SERVER_PLAYERS_CONNECTED, server.get_connectedPlayers());
        values.put(KEY_SERVER_PLAYERS_MAX, server.get_maxPlayers());
        values.put(KEY_SERVER_PING, server.get_ping());

        // updating row
        return db.update(TABLE_SERVER_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(server.get_id()) });
    }

    public void deleteServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVER_LIST, KEY_ID + " = ?",
                new String[] { String.valueOf(server.get_id()) });
        db.close();
    }
}
