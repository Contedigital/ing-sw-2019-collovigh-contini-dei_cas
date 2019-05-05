package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class handles the socket input stream server-side. A new SocketConnectionReader thread is started for every
 * Socket connection to be handled. This class specifically handles the messages coming from client side.
 * As in SocketClientReader class, the SocketConnectionReader start a SocketConnectionWriter thread to handle
 * output stream to the client and then keep listening for String messages, splits them every time it encounters
 * a special character ('\f'), look for the function linked to the header String and uses the other String as
 * function parameters and execute the function in another thread.
 */
public class SocketConnectionReader extends Thread {

    /**
     * Reference to the socket to be handled, which is passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * Reference to the SocketConnectionWriter handling the output stream for the same server socket
     */
    private SocketConnectionWriter socketConnectionWriter;

    /**
     * Used to store the splitted message received from Client and then process it, depending on its header
     */
    private String[] commands;

    /**
     * Attribute representing a BufferedReader to manage input stream from socket
     */
    BufferedReader input;

    /**
     * Map which relates each string message which can be received to a function
     */
    private Map<String, FunctionInterface> headersMap;

    /**
     * Constructor
     * @param socket represent the channel to communicate between client and server, which is created by the
     *               Server when a client tries to connect to the SocketServer
     */
    public SocketConnectionReader(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @return a reference to the socketConnectionWriter which is created at the beginning of run() method
     */
    public SocketConnectionWriter getSocketConnectionWriter() {
        return socketConnectionWriter;
    }

    /**
     * Initialize the SocketConnectionReader, creates a new SocketConnectionWriter thread and runs it, populate the
     * headersMap containing a link between String headers and functions, and finally enters an infinite loop
     * in which the thread keep listening for incoming messages, splits them as said above class declaration,
     * look for the related function and execute it in another separate thread.
     */
    @Override
        public void run() {
            Logger.getLogger("infoLogger").info("Received Client Connection");

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                socketConnectionWriter = new SocketConnectionWriter(socket);
                socketConnectionWriter.start();

                populateHeadersMap();

                while(true) {

                    String msg = input.readLine();
                    if(msg != null)
                        handleMsg(splitCommand(msg));
                }


            } catch(IOException e) {
                    e.getMessage();
            } finally {
                try {
                    socket.close();
                } catch(IOException e) {
                    e.getMessage();
                }
            }

        }

    /**
     *
     * @param msg String to be splitted
     * @return an array of Strings splitted by the special character '\f'
     */
    private String[] splitCommand(String msg) {
        if(msg == null){
            return null;
        }
        return msg.split("\f");
    }

    /**
     * Handle the array of Strings previously splitted, look for its corresponding function in the headersMap,
     * runs it in a separate thread
     * @param message the result of splitCommand
     */
    private void handleMsg(String[] message) {

        this.commands = message;
        FunctionInterface function = headersMap.get(message[0]);

        if (function == null)
            Logger.getLogger("infoLogging").warning("[DEBUG] [SERVER] ERRORE nella lettura della funzione dalla hashmap!");
        else
            try {
                new Thread (() -> {
                    function.execute();
                }).start();
            } catch(NumberFormatException e) {
                Logger.getLogger("infoLogging").warning("[DEBUG] [SERVER] ERRORE nel formato del messaggio socket ricevuto! ");
            }
    }


    /**
     * Initialize the Map by binding a String to its related function
     * @throws NumberFormatException
     */
    private void populateHeadersMap() throws NumberFormatException {

        headersMap = new HashMap<>();

        // login username
        headersMap.put("login", () -> {
            try {
                int id = Server.getWaitingRoom().addPlayer(commands[1], PlayerColor.valueOf(commands[2].toUpperCase()));
                if(id >= 0){
                    socketConnectionWriter.send("login\fOK");
                }

            }catch (NameAlreadyTakenException e){       //NOTE: temporary solution by D, just to make it compile
                socketConnectionWriter.send("login\fNAME_ALREADY_TAKEN");
            }catch (ColorAlreadyTakenException e){
                socketConnectionWriter.send("login\fCOLOR_ALREADY_TAKEN");
            }
        });

        //ping
        headersMap.put("pong", () -> {
            Logger.getLogger("infoLogging").info("Client reply to ping: " + commands[1]);
        });
    }

}




