package net.crowlhome.app.minecraftservercontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethen on 2/26/17.
 * Copyright Ethen Crowl
 */

class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 8;

    // Database name
    private static final String DATABASE_NAME = "mainDatabase";

    // Server list table name
    private static final String TABLE_SERVER_LIST = "server";

    // Player list table name
    private static final String TABLE_PLAYER_LIST = "player_list";

    // Server List Column names
    private static final String KEY_ID = "server_id";
    private static final String KEY_SERVER_NAME = "server_name";
    private static final String KEY_SERVER_ADDRESS = "server_address";
    private static final String KEY_SERVER_PORT_GAME = "server_port";
    private static final String KEY_SERVER_PORT_QUERY = "query_port";
    private static final String KEY_SERVER_PORT_RCON = "rcon_port";
    private static final String KEY_SERVER_RCON_PASS = "rcon_password";
    private static final String KEY_SERVER_PLAYERS_CONNECTED = "connected_players";
    private static final String KEY_SERVER_PLAYERS_MAX = "max_players";
    private static final String KEY_SERVER_CURRENT_PLAYER_NAMES = "current_player_names";
    private static final String KEY_SERVER_MOTD = "server_motd";
    private static final String KEY_SERVER_ICON = "server_icon";

    // Player list Column names
    private static final String KEY_PLAYER_SERVER_ID = "server_id";
    private static final String KEY_PLAYER_UUID = "uuid";
    private static final String KEY_PLAYER_NAME = "name";
    private static final String KEY_PLAYER_FACE = "face";
    private static final String KEY_PLAYER_SCOREBOARD = "scoreboard";
    private static final String KEY_PLAYER_IS_ONLINE = "is_online";


    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SERVER_LIST_TABLE = "CREATE TABLE " + TABLE_SERVER_LIST + "(" +
                KEY_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_SERVER_NAME + " TEXT," +
                KEY_SERVER_ADDRESS + " TEXT," +
                KEY_SERVER_PORT_GAME + " INTEGER," +
                KEY_SERVER_PORT_QUERY + " INTEGER," +
                KEY_SERVER_PORT_RCON + " INTEGER," +
                KEY_SERVER_RCON_PASS + " TEXT," +
                KEY_SERVER_PLAYERS_CONNECTED + " INTEGER," +
                KEY_SERVER_PLAYERS_MAX + " INTEGER," +
                KEY_SERVER_CURRENT_PLAYER_NAMES + " TEXT," +
                KEY_SERVER_MOTD + " TEXT," +
                KEY_SERVER_ICON + " BLOB)";
        db.execSQL(CREATE_SERVER_LIST_TABLE);

        String CREATE_PLAYER_LIST_TABLE = "CREATE TABLE " + TABLE_PLAYER_LIST + "(" +
                KEY_PLAYER_SERVER_ID + " INTEGER NOT NULL," +
                KEY_PLAYER_UUID + " TEXT NOT NULL," +
                KEY_PLAYER_NAME + " TEXT," +
                KEY_PLAYER_FACE + " BLOB," +
                KEY_PLAYER_SCOREBOARD + " TEXT," +
               " PRIMARY KEY (" + KEY_PLAYER_SERVER_ID + "," + KEY_PLAYER_UUID + "))";
        db.execSQL(CREATE_PLAYER_LIST_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Alter your tables here
        if (oldVersion < 8) {
            db.execSQL("ALTER TABLE " + TABLE_PLAYER_LIST + " ADD COLUMN " + KEY_PLAYER_IS_ONLINE +
                    " INTEGER DEFAULT 0");
        }
        /*
        if (oldVersion < 8) {
            // Make your next change here.
        }
        */
    }

    void addServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_NAME, server.get_serverName());
        values.put(KEY_SERVER_ADDRESS, server.get_serverAddress());
        values.put(KEY_SERVER_PORT_GAME, server.get_serverPort());
        values.put(KEY_SERVER_PORT_QUERY, server.get_queryPort());
        values.put(KEY_SERVER_PORT_RCON, server.get_rconPort());
        values.put(KEY_SERVER_RCON_PASS, server.get_rconPass());

        // Insert Row
        db.insert(TABLE_SERVER_LIST, null, values);
        db.close(); // Close database connection
    }

    Server getServer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERVER_LIST, new String[] { KEY_ID,
        KEY_SERVER_NAME, KEY_SERVER_ADDRESS, KEY_SERVER_PORT_GAME, KEY_SERVER_PORT_QUERY, KEY_SERVER_PORT_RCON,
        KEY_SERVER_RCON_PASS, KEY_SERVER_PLAYERS_CONNECTED, KEY_SERVER_PLAYERS_MAX, KEY_SERVER_CURRENT_PLAYER_NAMES,
                KEY_SERVER_MOTD, KEY_SERVER_ICON}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Server server = new Server(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7),
                cursor.getInt(8), cursor.getString(9), cursor.getString(10), cursor.getBlob(11));

        cursor.close();
        return server;
    }

    List<Server> getAllServers() {
        List<Server> serverList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SERVER_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add them to the list
        if (cursor.moveToFirst()) {
            do {
                Server server = new Server(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7),
                        cursor.getInt(8), cursor.getString(9), cursor.getString(10), cursor.getBlob(11));
                serverList.add(server);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return serverList;
    }

    int updateServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_NAME, server.get_serverName());
        values.put(KEY_SERVER_ADDRESS, server.get_serverAddress());
        values.put(KEY_SERVER_PORT_GAME, server.get_serverPort());
        values.put(KEY_SERVER_PORT_QUERY, server.get_queryPort());
        values.put(KEY_SERVER_PORT_RCON, server.get_rconPort());
        values.put(KEY_SERVER_RCON_PASS, server.get_rconPass());
        values.put(KEY_SERVER_PLAYERS_CONNECTED, server.get_connectedPlayers());
        values.put(KEY_SERVER_PLAYERS_MAX, server.get_maxPlayers());
        values.put(KEY_SERVER_CURRENT_PLAYER_NAMES, server.get_currentPlayerNames());
        values.put(KEY_SERVER_MOTD, server.get_serverMOTD());
        values.put(KEY_SERVER_ICON, server.get_serverIcon());

        // updating row
        return db.update(TABLE_SERVER_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(server.get_id()) });
    }

    void deleteServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVER_LIST, KEY_ID + " = ?",
                new String[] { String.valueOf(server.get_id()) });
        db.delete(TABLE_PLAYER_LIST, KEY_PLAYER_SERVER_ID + " = ?",
                new String[] { String.valueOf(server.get_id()) });
        db.close();
    }

    void addPlayer(Player player) {
        if (checkPlayerExists(player.get_server_id(), player.get_uuid())) {
            updatePlayer(player);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PLAYER_SERVER_ID, player.get_server_id());
            values.put(KEY_PLAYER_UUID, player.get_uuid());
            values.put(KEY_PLAYER_NAME, player.get_name());

            // Insert Row
            db.insert(TABLE_PLAYER_LIST, null, values);
            db.close(); // Close database connection
        }
    }

    int updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, player.get_name());
        values.put(KEY_PLAYER_FACE, player.get_face());
        values.put(KEY_PLAYER_SCOREBOARD, player.get_scoreboard());
        values.put(KEY_PLAYER_IS_ONLINE, player.get_is_online());

        // Update Row
        return db.update(TABLE_PLAYER_LIST, values, KEY_PLAYER_SERVER_ID + " = ? AND " + KEY_PLAYER_UUID + " = ?",
                new String[] { String.valueOf(player.get_server_id()),
                        String.valueOf(player.get_uuid()) });
    }

    List<Player> getAllPlayers(int server_id) {
        List<Player> playerList = new ArrayList<>();
        // Select All Query
        String selectQuery = ("SELECT * FROM " + TABLE_PLAYER_LIST + " WHERE " + KEY_PLAYER_SERVER_ID +
                " = " + String.valueOf(server_id));

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add them to the list
        if (cursor.moveToFirst()) {
            do {
                Player player = new Player(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getInt(5));
                playerList.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return playerList;
    }

    Player getPlayer(int server_id, String uuid) {
        // Select All Query
        String selectQuery = ("SELECT * FROM " + TABLE_PLAYER_LIST + " WHERE " + KEY_PLAYER_SERVER_ID +
                " = " + String.valueOf(server_id) + " AND " + KEY_PLAYER_UUID + " = " + uuid);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Player player = new Player(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getInt(5));
        cursor.close();
        return player;
    }



    private boolean checkPlayerExists(int server_id, String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_PLAYER_LIST + " WHERE " + KEY_PLAYER_SERVER_ID + " = " +
                server_id + " AND " + KEY_PLAYER_UUID + " = '" + uuid + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    boolean checkPlayerExistsByName(int server_id, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_PLAYER_LIST + " WHERE " + KEY_PLAYER_SERVER_ID + " = " +
                server_id + " AND " + KEY_PLAYER_NAME + " = '" + name + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
