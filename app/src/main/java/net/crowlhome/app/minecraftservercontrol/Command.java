package net.crowlhome.app.minecraftservercontrol;

/**
 * Created by ethen on 5/5/17.
 * Copyright Ethen Crowl
 */

public class Command {

    private String mCommand;
    private String mServerAddress;
    private int mRconPort;
    private String mPassword;
    private int mMode;
    private String mOutput;

    public Command() {
        // Required empty constructor
    }

    public Command(String command, String serverAddress, int port, String password, int mode) {
        this.mCommand = command;
        this.mServerAddress = serverAddress;
        this.mPassword = password;
        this.mRconPort = port;
        this.mMode = mode;
    }

    public String getCommand() {
        return mCommand;
    }

    public void setCommand(String command) {
        mCommand = command;
    }

    public String getServerAddress() {
        return mServerAddress;
    }

    public void setServerAddress(String serverAddress) {
        mServerAddress = serverAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public int getRconPort() {
        return mRconPort;
    }

    public void setRconPort(int rconPort) {
        mRconPort = rconPort;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public String getOutput() {
        return mOutput;
    }

    public void setOutput(String output) {
        mOutput = output;
    }
}
