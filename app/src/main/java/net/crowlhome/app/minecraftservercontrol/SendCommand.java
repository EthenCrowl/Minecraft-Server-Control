package net.crowlhome.app.minecraftservercontrol;

import android.os.AsyncTask;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.IOException;
import java.util.List;

/**
 * Created by ethen on 5/5/17.
 * Copyright Ethen Crowl
 */

class SendCommand extends AsyncTask<List<Command>, Void, Command>{
    SendCommandResponse delegate = null;

    private String result;
    private Command output;

    protected Command doInBackground(List<Command>... commandList) {
        for (Command payload : commandList[0]) {
            if (payload.getCommand() != null & payload.getPassword() != null &
                    payload.getServerAddress() != null) {
                output = payload;
                try {
                    Rcon rcon = new Rcon(output.getServerAddress(), output.getRconPort(),
                            output.getPassword().getBytes());
                    result = rcon.command(output.getCommand());
                    output.setOutput(result);
                } catch (IOException io) {
                    io.printStackTrace();
                    output.setOutput("Error Reaching Server");
                } catch (AuthenticationException auth) {
                    auth.printStackTrace();
                    output.setOutput("Authentication Error");
                }
            }
        }
        return null;
    }

    protected void onPostExecute(Command command) {
        delegate.sendCommandProcessFinish(output);
    }
}
