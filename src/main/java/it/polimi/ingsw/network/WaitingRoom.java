package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.OverMaxPlayerException;
import it.polimi.ingsw.view.cachemodel.sendables.CachedLobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class represents the Lobby for the player to log in before the game starts
 */
public class WaitingRoom {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    public static final Level level = Level.INFO;

    private static final int TIMER = 2;
    private static int timerCount = TIMER;

    private static final int DEFAULT_MIN_PLAYERS = 2;
    private static final int DEFAULT_MAX_PLAYERS = 5;

    private CopyOnWriteArrayList<String> players;
    private List<PlayerColor> colors;
    private Boolean active;
    private int activeGame;
    private Games games;
    private int mapType = 0;


    /**
     * Constructor: creates a new waitingRoom for the player to join both if they want to join a new game or load an old game
     * @param gameId is -1 if the game will be a new one, or the game id if the game is saved
     */
    public WaitingRoom(int gameId) throws GameNonExistentException {

        // things to do for both loaded games and new ones
        this.active = true;
        this.games = new Games();

        // -1 = new game
        if (gameId == -1) {
            this.colors = new ArrayList<>();
            this.players = new CopyOnWriteArrayList<>();


            LOGGER.log(Level.FINE,"[OK] Started Waiting Room for new Game");
        }else{
            if (!games.contains(gameId)) throw new GameNonExistentException();
            activeGame = gameId;
            // need to catch all saved games and start the correspondent one

            LOGGER.log(Level.FINE, "[OK] Started Waiting Room for saved game w/ id: {0}", gameId);
        }
    }

    public Boolean isActive(){

        return this.active;
    }

    /**
     * This method starts a new Game
     * if the players specified a map that will be taken or it will be generated casually
     * @return the new Controller
     */
    public synchronized void initGame(){

        activeGame = games.addGame();

        if(this.mapType == 0) {
            Server.setController(new Controller(this.players, this.colors, this.activeGame));
            Server.getController().handleTurnPhase();
        } else{
            Server.setController(new Controller(this.players,this.colors,this.activeGame,this.mapType));
            Server.getController().handleTurnPhase();
        }


        this.active = false;
    }

    /**
     *
     * @param name is the name of the new player
     * @param playerColor is the color of the new player
     * @return id of the player if name and color or throws exceptions if those are already taken
     * @throws NameAlreadyTakenException if the name is already taken
     * @throws ColorAlreadyTakenException of the color is already taken
     */
    public synchronized int addPlayer(String name, PlayerColor playerColor) throws NameAlreadyTakenException,ColorAlreadyTakenException,OverMaxPlayerException{

        if (isNameAlreadyTaken(name)) {

            throw new NameAlreadyTakenException(name);
        }

        if (isColorAlreadyTaken(playerColor)){

            throw new ColorAlreadyTakenException(playerColor);
        }

        if (players.size() < DEFAULT_MAX_PLAYERS) {

            players.add(name);

            colors.add(playerColor);


            if (players.size() >= DEFAULT_MIN_PLAYERS) {

                System.out.println("[WaitingRoom] start timer");
                Thread thread = new Thread(() -> { startTimer(); });
                thread.start();
            }

            return players.indexOf(name);

        }else throw new OverMaxPlayerException();

    }

    public synchronized void notifyNewPlayer(){

        for (ToView client : Server.getClients().values()){

            client.sendUpdate(new CachedLobby(players));

            LOGGER.log(level,"Sent LOBBY_UPDATE to client : {0}", client);
        }

    }

    /**
     *
     * @param name is the name to check
     * @return true if the name is free
     */
    public Boolean isNameAlreadyTaken(String name){

        return (players.contains(name));
    }

    public String getName(int playerId){

        return this.players.get(playerId);
    }

    /**
     *
     * @param color is the color to check
     * @return true if the color is free
     */
    public Boolean isColorAlreadyTaken(PlayerColor color){

        return (colors.contains(color));
    }

    /**
     *
     * @param id is the id of the player to remove
     */
    public synchronized void removePlayer(int id){

        players.remove(id);
        colors.remove(id);

        notifyNewPlayer();
    }

    /**
     *
     * @return the current value of timerCount which is decreased by startTimer() method
     */
    public static int getTimerCount() {
        return timerCount;
    }

    /**
     * This method will start a thread which runs a timer, decrementing a counter every second until it reaches 0
     */
    public void startTimer(){

        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {

                if (players.size() >= DEFAULT_MIN_PLAYERS) {

                    System.out.println("[DEBUG] WaitingRoomTimer counter: " + timerCount);
                    timerCount--;
                    if ((timerCount <= 0) || (players.size() == DEFAULT_MAX_PLAYERS)) {
                        System.out.println("[DEBUG] Timer has expired!");
                        //chiama initGame o gestire timer scaduto
                        System.out.println("[DEBUG] AVVIO DELLA PARTITA IN CORSO...");
                        initGame();
                        //termina l'esecuzione del thread
                        this.cancel();
                    }

                }else {

                    System.out.println("[WAITING-ROOM] Timer annullato ");

                    timerCount = TIMER;

                    this.cancel();
                }
            }
        };

        Timer timer = new Timer("WaitingRoomTimer");
        timer.scheduleAtFixedRate(timerTask, 30, 1000);

    }

    public synchronized int getMapType() {
        return mapType;
    }

    public synchronized void setMapType(int mapType) {

        if ((this.mapType == 0) && (mapType != 0)) {
            this.mapType = mapType;
        }
    }

    public int size(){

        return this.players.size();
    }

    public Games getGames() {
        return games;
    }


}
