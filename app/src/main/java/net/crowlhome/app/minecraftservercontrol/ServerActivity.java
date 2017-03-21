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
        String server_id_string = getIntent().getStringExtra("SERVER_ID");
        server_id = Integer.parseInt(server_id_string);
        currentServer = db.getServer(server_id);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_server);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewCurrentPlayers(currentServer);
                refreshPlayerListView();
            }
        });


        if (currentServer.get_connectedPlayers() > 0) {
            updateCurrentPlayerList();
            updatePlayersInDatabase();
        }

        allPlayers = db.getAllPlayers(server_id);

        for (Player player : allPlayers) {
            if (!player.hasFace()) {
                getPlayerFace(player);
            }
        }

        allPlayers = db.getAllPlayers(server_id);

        if (allPlayers != null) {
            createPlayerListView();
        }




    }

    private void refreshPlayerListView() {
        allPlayers = db.getAllPlayers(server_id);
        mAdapter.clear();
        mAdapter.addAll(allPlayers);
        mAdapter.notifyDataSetChanged();
    }

    private void createPlayerListView() {
        mAdapter = new PlayerAdapter(this, (ArrayList<Player>) allPlayers, server_id);
        list = (ListView) findViewById(R.id.player_list_view);
        list.setAdapter(mAdapter);
    }

    private void updateCurrentPlayerList() {
        try {
            String currentPlayers = currentServer.get_currentPlayerNames();
            String[] reSplit = currentPlayers.split(", ");
            currentConnectedPlayers = Arrays.asList(reSplit);
        } catch (Exception e) {
            e.printStackTrace();
            currentConnectedPlayers = new ArrayList<>();
        }
    }

    private void updatePlayersInDatabase() {
        if (currentConnectedPlayers != null) {
            for (String name : currentConnectedPlayers) {
                Player player = new Player(server_id, name);
                playerList.add(player);
            }
            for (Player player : playerList) {
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
        db.updatePlayer(result);
    }

    @Override
    public void downloadPlayerUUIDProcessFinish(Player result) {
        db.addPlayer(result);
    }

    @Override
    public void queryProcessFinish(Server server) {
        db.updateServer(server);
        currentServer = db.getServer(server_id);
        updateCurrentPlayerList();
        swipeRefreshLayout.setRefreshing(false);
    }
}
