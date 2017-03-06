package net.crowlhome.app.minecraftservercontrol;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddServerActivity extends AppCompatActivity {

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_server);

        db = new DatabaseHandler(this);

        Button button = (Button) findViewById(R.id.add_server_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addServer();
                finish();
            }
        });
    }

    private void addServer() {
        String serverName;
        String serverAddress;
        String serverPortString;
        int serverPort;
        String queryPortString;
        int queryPort;
        String rconPortString;
        int rconPort;
        String rconPass;

        EditText server_name = (EditText) findViewById(R.id.server_name);
        EditText server_address = (EditText) findViewById(R.id.server_address);
        EditText server_port = (EditText) findViewById(R.id.server_port);
        EditText query_port = (EditText) findViewById(R.id.query_port);
        EditText rcon_port = (EditText) findViewById(R.id.rcon_port);
        EditText rcon_pass = (EditText) findViewById(R.id.rcon_password);

        serverName = server_name.getText().toString();
        serverAddress = server_address.getText().toString();
        serverPortString = server_port.getText().toString();
        serverPort = Integer.parseInt(serverPortString);
        queryPortString = query_port.getText().toString();
        rconPortString = rcon_port.getText().toString();
        rconPass = rcon_pass.getText().toString();
        queryPort = Integer.parseInt(queryPortString);
        rconPort = Integer.parseInt(rconPortString);

        Server server = new Server(serverName, serverAddress, serverPort, queryPort, rconPort, rconPass);

        db.addServer(server);

    }
}
