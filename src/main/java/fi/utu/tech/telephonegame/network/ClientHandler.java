package fi.utu.tech.telephonegame.network;

import fi.utu.tech.telephonegame.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.TransferQueue;

public class ClientHandler implements Runnable{
    private Socket soketti;
    private ObjectOutputStream outputti;
    private NetworkService nettiserviisi;
    private TransferQueue<Object> inputjono;

    /**
     * Constructs the ClientHandler-object
     *
     * @param soketti The Socket-object that connects to the server.
     * @param nettiserviisi The NetworkService-object, where CH is created in.
     */
    public ClientHandler(Socket soketti, NetworkService nettiserviisi){
        this.soketti = soketti;
        this.nettiserviisi = nettiserviisi;
        this.inputjono = nettiserviisi.getInputQueue();
    }

    /**
     * Constructor, that creates the ClientHandler-object. This time the Socket has to made
     * seperately.
     *
     * @param ip IP-address of the Socket
     * @param peerPort TCP-port of the Socket
     * @param nettiserviisi NetworkService-object, where the CH is created in.
     */
    public ClientHandler(String ip, int peerPort, NetworkService nettiserviisi){
        this.nettiserviisi = nettiserviisi;
        this.inputjono = nettiserviisi.getInputQueue();

        try {
            soketti = new Socket(ip,peerPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method, that reads the messages of the Object Streams and puts them on the TransferQueue
     */
    @Override
    public void run() {
        try {
            outputti = new ObjectOutputStream(soketti.getOutputStream());
            var is = new ObjectInputStream(soketti.getInputStream());

            while(true){
                Object incoming = is.readObject();

                if (incoming instanceof Message){
                    Message incMessage = (Message) incoming;
                    System.out.println(incMessage.getMessage());
                    inputjono.put(incoming);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method, that sends out the message to the neighbouring nodes.
     *
     * @param out The serializable object to be sent to all the connected nodes
     */
    public void send(Serializable out) {
        try {
            outputti.writeObject(out);
            outputti.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
