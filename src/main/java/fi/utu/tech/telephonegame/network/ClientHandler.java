package fi.utu.tech.telephonegame.network;

import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket portti;
    public ClientHandler(Socket portti){
        this.portti = portti;
    }
    @Override
    public void run() {
        //jotai
    }
}
