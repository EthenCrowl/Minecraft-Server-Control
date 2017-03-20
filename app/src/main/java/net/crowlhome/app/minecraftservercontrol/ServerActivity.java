package net.crowlhome.app.minecraftservercontrol;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerActivity extends AppCompatActivity implements DownloadPlayerFaceResponse,
        DownloadPlayerUUIDResponse, QueryServerResponse{

    private int server_id;
    private DatabaseHandler db;
    private PlayerAdapter mAdapter;
    private DownloadPlayerUUID downloadPlayerUUID;
    private DownloadPlayerFace downloadPlayerFace;
    private QueryServer queryServer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView list;
    private List<Player> playerList = new ArrayList<Player>();
    private List<Player> allPlayers;
    private List<String> currentConnectedPlayers;
    private Server currentServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        db = new DatabaseHandler(this);
        server_id = Integer.parseInt(getIntent().getStringExtra("SERVER_ID"));
        currentServer = db.getServer(server_id);


        updateCurrentPlayerList();
        updatePlayersInDatabase();
        createPlayerListView();



    }

    private void refreshPlayerListView() {
        allPlayers = db.getAllPlayers(server_id);
        mAdapter.clear();
        mAdapter.addAll(allPlayers);
        mAdapter.notifyDataSetChanged();
    }

    private void createPlayerListView() {
        mAdapter = new PlayerAdapter(this, (ArrayList<Player>) allPlayers,
                (ArrayList<Player>) playerList);
        list = (ListView) findViewById(R.id.server_list);
        list.setAdapter(mAdapter);
    }

    private void updateCurrentPlayerList() {
        String currentPlayers = currentServer.get_currentPlayerNames();
        String joinedMinusBrackets = currentPlayers.substring( 1,
                currentPlayers.length() - 1);
        String[] reSplit = joinedMinusBrackets.split( ", ");
        currentConnectedPlayers = Arrays.asList(reSplit);
    }

    private void updatePlayersInDatabase() {
        for (String name : currentConnectedPlayers) {
            Player player = new Player(server_id, name);
            playerList.add(player);
        }
        for (Player player : playerList) {
            if (!db.checkPlayerExists(server_id, player.get_name())) {
                getUUID(player);
            }
        }
    }

    private void getNewCurrentPlayers(Server server) {
        queryServer = new QueryServer();
        queryServer.delegate = this;
        queryServer.execute(server);
    }

    private void getPlayerFace(Player player) {
        downloadPlayerFace = new DownloadPlayerFace();
        downloadPlayerFace.delegate = this;
        downloadPlayerFace.execute(player);
    }

    private void getUUID(Player player) {
        downloadPlayerUUID = new DownloadPlayerUUID();
        downloadPlayerUUID.delegate = this;
        downloadPlayerUUID.execute(player);
    }

    @Override
    public void downloadPlayerFaceProcessFinish(Player result) {
        db.addPlayer(result);
        allPlayers = db.getAllPlayers(server_id);
    }

    @Override
    public void downloadPlayerUUIDProcessFinish(Player result) {
        getPlayerFace(result);
    }

    @Override
    public void queryProcessFinish(Server server) {
        db.updateServer(server);
        currentServer = db.getServer(server_id);
    }
}
