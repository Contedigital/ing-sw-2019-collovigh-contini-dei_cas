package it.polimi.ingsw.network.client.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.network.socket.FunctionInterface;
import it.polimi.ingsw.runner.RunClient;
import it.polimi.ingsw.view.cachemodel.sendables.*;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.otherplayerturn.GrabTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.MoveTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.PowerUpTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.ShootTurnUpdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

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
    private static Level level = FINE;

    private Gson gson = new Gson();

    private static final String END_GAME = "endGame";
    private static final String ASK_MAP_AND_SKULLS = "askMapAndSkulls";
    private static final String START_GAME = "startGame";
    private static final String START_SPAWN = "startSpawn";
    private static final String START_POWER_UP = "startPowerUp";
    private static final String START_ACTION = "startAction";
    private static final String START_RELOAD = "startReload";
    private static final String ASK_GRENADE = "askGrenade";
    private static final String SHOW = "showMessage";
    private static final String SET_INT_ANSWER = "setIntAnswer";
    private static final String REDO_FRENZY_SHOOT = "reDoFrenzyAtomicShoot";
    private static final String CLOSE = "close";

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
    public SocketClientReader(Socket socket){
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
            scw = new SocketClientWriter(socket);
            scw.run();

            populateHeadersMap();

            while(true) {

                // Read the line received

                String msg = receive();
                // if the line starts with a '{' -> json
                if(msg != null) {

                    LOGGER.log(level,"[SOCKET-CLIENT-READER] received: {0}", msg);

                    if (msg.startsWith("{")) handleJson(msg);
                        // else will be handled by the HashMap
                    else handleMsg(splitCommand(msg));

                }
            }


        } catch (IOException e) {
            LOGGER.log(WARNING,"Client Error: " + e.getMessage());
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
        }else if(msg.startsWith("{")){
            handleJson(msg);
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
            //errore
            LOGGER.log(WARNING, "[DEBUG] [CLIENT] ERRORE nella lettura della funzione dalla hashmap!");
        else
            try {
                new Thread(() ->  function.execute(message) ).start();
            } catch (NumberFormatException e) {
                LOGGER.log(level, "[DEBUG] [CLIENT] ERRORE nel formato del messaggio socket ricevuto! ");
            }
    }

    /**
     * Handle the Json received from Server
     * @param msg a String representing the Json received from the Server (SocketConnectionWriter)
     */
    private void handleJson(String msg) {

        // split the gson to get the last parameter (GSON can not detect the superclass)

        String[] update = msg.split(",");

        // LOG the update

        LOGGER.log(WARNING, "[DEBUG] [SOCKET-CLIENT-READER] Received Json {0} : Calling handleJson method. ", update[ update.length - 1 ]);

        // creates a new UpdateClass variable ( will be instantiated in the switch )

        UpdateClass updateClass = null;

        // creates a new Gson variable

        gson = new Gson();

        // switchCase on the type

        switch (update[update.length - 1]) {

            case "\"type\":\"INITIAL\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, InitialUpdate.class);

                break;

            case "\"type\":\"POWERUP_BAG\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedPowerUpBag.class);

                break;

            case "\"type\":\"STATS\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedStats.class);

                break;

            case "\"type\":\"WEAPON_BAG\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg,CachedWeaponBag.class);

                break;

            case "\"type\":\"AMMO_BAG\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedAmmoBag.class);

                break;

            case "\"type\":\"GAME\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedGame.class);

                break;

            case "\"type\":\"SPAWN_CELL\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedSpawnCell.class);

                break;

            case "\"type\":\"AMMO_CELL\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedAmmoCell.class);

                break;

            case "\"type\":\"LOBBY\"}":

                // rebuilds the class

                updateClass = gson.fromJson(msg, CachedLobby.class);

                break;

            case "\"type\":\"TURN\"}":

                // json will be handled in secondary method

                updateClass = handleTurnUpdate(msg);

                break;

            default:

                LOGGER.log(WARNING,"[Socket-Client-Reader] Received unknown Update");

                break;

        }

        System.out.println("updateClass: " + updateClass);
        System.out.println("RunClient.getView " + RunClient.getView());

        RunClient.getView().sendUpdates(updateClass);
    }

    private UpdateClass handleTurnUpdate(String message){

        LOGGER.log(INFO,() -> "[SOCKET_CLIENT_READER] RECEIVED TURN UPDATE : " + message );

        // split the gson to get the last parameter (GSON can not detect the superclass)

        String[] splitted = message.split(",");

        // creates a new UpdateClass variable ( will be instantiated in the switch )

        UpdateClass updateClass = null;

        String type = (splitted[ splitted.length - 3 ].startsWith("{")) ? splitted[ splitted.length - 3 ].substring(1,splitted[ splitted.length - 3 ].length()) : splitted[ splitted.length - 3 ];


        switch (type){

            case "\"actionType\":\"MOVE\"":

                // rebuilds the class

                updateClass = gson.fromJson(message,MoveTurnUpdate.class);

                break;

            case "\"actionType\":\"GRAB\"":

                // rebuilds the class

                updateClass = gson.fromJson(message, GrabTurnUpdate.class);

                break;

            case "\"actionType\":\"SHOOT\"":

                // rebuilds the class

                updateClass = gson.fromJson(message, ShootTurnUpdate.class);

                break;

            case "\"actionType\":\"POWERUP\"":

                // rebuilds the class

                updateClass = gson.fromJson(message, PowerUpTurnUpdate.class);

                break;

            default:

                LOGGER.log(WARNING,"[Socket-Client-Reader] Received unknown TurnUpdate");

                break;

        }


        return updateClass;

    }


    /**
     * Initialize the Map by binding a String to its related function
     * @throws NumberFormatException
     */
    private void populateHeadersMap() {

        headersMap = new HashMap<>();

        // login username
        headersMap.put("login", (commands) -> {
            LOGGER.log(level,() -> "[Socket-Client-Reader] login reply: " + commands[1]);
            if(!(commands[1].equals("OK")))
                RunClient.getView().retryLogin(commands[1]);
            else{
                RunClient.getView().show(commands[1]);
            }
            RunClient.getView().setPlayerId(Integer.parseInt(commands[2]));

            scw.setIntAnswer(Integer.parseInt(commands[2]));
        });


        //ping
        headersMap.put("ping", (commands) -> {
            //System.out.println("[DEBUG] Ricevuta ping request dal server.");
            //scw.send("pong\f" + scw.getPlayerId());
        });

        //initGame
        headersMap.put(START_GAME, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] Ricevuto initGame dal server");
            RunClient.getView().startGame();
        });



        //startSpawn
        headersMap.put(START_SPAWN, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] Ricevuto startSpawn dal server");
            RunClient.getView().startSpawn();
        });

        //startPowerUp
        headersMap.put(START_POWER_UP, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] Ricevuto startPowerUp dal server");
            RunClient.getView().startPowerUp();
        });

        //startAction
        headersMap.put(START_ACTION, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] Ricevuto startAction dal server");
            RunClient.getView().startAction(Boolean.valueOf(commands[1]), Boolean.valueOf(commands[2]));
        });

        //reload
        headersMap.put(START_RELOAD, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] Ricevuto startReload dal server");
            RunClient.getView().startReload();
        });

        //askGrenade
        headersMap.put(ASK_GRENADE, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] received askGrenade by server");
            RunClient.getView().askGrenade();
        });

        //askValidMove
        headersMap.put("askMoveValid", (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] received askValidMove by server");
            if(commands[1].equals("true")) {
                RunClient.getView().setValidMove(true);
            } else if(commands[1].equals("false")){
                RunClient.getView().setValidMove(false);
            }
        });

        //reDoFrenzyAtomicShoot
        headersMap.put(REDO_FRENZY_SHOOT, (commands) -> {
            LOGGER.log(level,"[Socket-Client-Reader] received reDoFrenzyAtomicShoot by server");
            RunClient.getView().reDoFrenzyAtomicShoot();
        });

        //show
        headersMap.put(SHOW, (commands) -> {
            LOGGER.log(level,() -> "[Socket-Client-Reader] received show by server " + Arrays.toString(commands));
            RunClient.getView().show(commands[1]);
        });

        //endGame
        headersMap.put(END_GAME,(commands) -> {
            LOGGER.log(level,() -> "[Socket-Client-Reader] received endGame by server ");
            RunClient.getView().endGame();
        });

        //setIntAnswer
        headersMap.put(SET_INT_ANSWER,(commands) -> {

            LOGGER.log(level,() -> "[Socket-Client-Reader] received setIntAnswer by server ");

            scw.setIntAnswer(Integer.parseInt(commands[1]));

        });

        //close after timer expires
        headersMap.put(CLOSE,(commands) -> {

            LOGGER.log(level,() -> "[Socket-Client-Reader] received close by server ");

            RunClient.getView().close();

        });

        //ask map type and skulls
        headersMap.put(ASK_MAP_AND_SKULLS,(commands) -> {

            LOGGER.log(level,() -> "[Socket-Client-Reader] received askMapAndSkulls by server ");

            List<Integer> mapAndSkulls = RunClient.getView().askMapAndSkulls();

            scw.send(ASK_MAP_AND_SKULLS + "\f" + mapAndSkulls.get(0) + "\f" + mapAndSkulls.get(1));

        });
    }


}
