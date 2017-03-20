package net.crowlhome.app.minecraftservercontrol;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ServerActivity extends AppCompatActivity implements DownloadPlayerFaceResponse {

    private int server_id;
    private DatabaseHandler db;
    private PlayerAdapter mAdapter;
    //private DownloadServerImage downloadServerImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView list;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        db = new DatabaseHandler(this);
        server_id = Integer.parseInt(getIntent().getStringExtra("SERVER_ID"));

    }

    @Override
    public void downloadPlayerFaceProcessFinish(Player result) {

    }
}
