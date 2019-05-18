package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.RunClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * This class handles the socket input stream client-side, allowing to have an async bidirectional communication
 * between client and server. The input stream coming from socket is in fact handled with a thread that keeps reading messages
 * and process them, splitting the String received every time it encounters a special character ('\f') in his header,
 * which then is used to retrieve the function to be called inside an HashMap that stores every function needed, and the other
 * parts of the String are used as parameters to call that function.
 * The SocketClientReader class also handles the construction of SocketClientWriter, which handles the output stream
 * on the same socket in another separate thread.
 */
public class SocketClientReader extends Thread {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    /**
     * Attribute representing a BufferedReader to manage input stream from socket
     */
    private BufferedReader in;

    /**
     * Reference to the socket to be handled, which is passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * Reference to the SocketClientWriter handling the output stream for the same client socket
     */
    private SocketClientWriter scw;

    /**
     * Used to store the splitted message received from Server and then process it, depending on its header
     */
    private String[] commands;

    /**
     * Map which relates each string message which can be received to a function
     */
    private Map<String, FunctionInterface> headersMap;

    /**
     *
     * @return a reference to the SocketClientWriter linked to this client output stream
     */
    public SocketClientWriter getScw() {
        return scw;
    }

    /**
     * Constructor
     * @param socket represent the channel to communicate between client and server, which is created by the
     *               Server when a client tries to connect to the SocketServer
     */
    SocketClientReader(Socket socket){
        this.socket = socket;
    }

    public void setScw(SocketClientWriter scw) {
        this.scw = scw;
    }

    /**
     * Initialize the SocketClientReader, creates a new SocketClientWriter thread and runs it, populate the
     * headersMap containing a link between String headers and functions, and finally enters an infinite loop
     * in which the thread keep listening for incoming messages, splits them as said above class declaration,
     * look for the related function and execute it in another separate thread.
     */
    @Override
    public void run(){

        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            //scw = new SocketClientWriter(socket);
            //scw.start();

            populateHeadersMap();

            while(true) {
                String msg = receive();
                handleMsg(splitCommand(msg));
            }


        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }

    /**
     *
     * @return String received from SocketServer
     * @throws IOException
     */
    public String receive() throws IOException {
        return in.readLine();
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
    public void handleMsg(String[] message) {
        this.commands = message;
        FunctionInterface function = headersMap.get(message[0]);
        if (function == null)
            //errore
            LOGGER.log(WARNING, "[DEBUG] [CLIENT] ERRORE nella lettura della funzione dalla hashmap!");
        else
            try {
                new Thread(() -> {
                    function.execute();
                }).start();
            } catch (NumberFormatException e) {
                LOGGER.log(WARNING, "[DEBUG] [CLIENT] ERRORE nel formato del messaggio socket ricevuto! ");
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
            System.out.println("[DEBUG] [Server] login reply: " + commands[1]);
            if(!(commands[1].equals("OK")))
                RunClient.getUi().retryLogin();
            RunClient.getView().setPlayerId(Integer.parseInt(commands[2]));
        });

        //ping
        headersMap.put("ping", () -> {
            System.out.println("[DEBUG] Ricevuta ping request dal server.");
            scw.send("pong\f" + scw.getPlayerId());
        });

        //startSpawn
        headersMap.put("startSpawn", () -> {
            System.out.println("[DEBUG] Ricevuto startSpawn dal server");
            RunClient.getCli().startSpawn();
        });

        //startPowerUp
        headersMap.put("startPowerUp", () -> {
            System.out.println("[DEBUG] Ricevuto startPowerUp dal server");
            RunClient.getUi().startPowerUp();
        });

        //startAction
        headersMap.put("startAction", () -> {
            System.out.println("[DEBUG] Ricevuto startAction dal server");
            RunClient.getUi().startAction();
        });

        //reload
        headersMap.put("startReload", () -> {
            System.out.println("[DEBUG] Ricevuto startReload dal server");
            RunClient.getUi().startReload();
        });

    }


}
