package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethen on 5/5/17.
 * Copyright Ethen Crowl
 */

public class SendCommand extends AsyncTask<List<Command>, Void, String>{
    public SendCommandResponse delegate = null;

    private String result;

    protected String doInBackground(List<Command>... commandList) {
        for (Command command : commandList[0]) {
            if (command.getCommand() != null & command.getPassword() != null &
                    command.getServerAddress() != null) {
                try {
                    Rcon rcon = new Rcon(command.getServerAddress(), command.getRconPort(),
                            command.getPassword().getBytes());

                    if (rcon != null) {
                        result = rcon.command(command.getCommand());
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                    result = "Error Reaching Server";
                } catch (AuthenticationException auth) {
                    auth.printStackTrace();
                    result = "Authentication Error";
                }
            }
        }
        return null;
    }

    protected void onPostExecute(String output) {
        delegate.sendCommandProcessFinish(result);
    }
}
