package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToServer extends Remote {

    /**
     *
     * @param name is the name chosen by the player
     * @param color is the color chosen by the player
     * @return the player id or -1 if the login was unsuccessful
     * @throws RemoteException
     */
    int joinGame(String address, String remoteName, String name, PlayerColor color) throws RemoteException;

    /**
     *
     * @param mapType is the map chosen
     * @throws RemoteException
     */
    void voteMapType(int mapType) throws RemoteException;

    /**
     * This method will register the client address to the server making it reachable
     * @param address is the id of the local registry
     * @param playerId is the id of the player
     * @throws RemoteException
     */
    void registerMe(String address, int playerId, String name) throws RemoteException;

    Boolean ping() throws RemoteException;

    /**
     *
     * @param name is the name chosen
     * @return the id assigned to it
     */
    int reconnect(String name) throws RemoteException, GameNonExistentException;


    //SPAWN

     void spawn(CachedPowerUp powerUp) throws RemoteException;

    //POWERUP

     void useNewton(Color color, int playerId, Directions directions, int amount) throws RemoteException;
     void useTeleport(Color color, int r, int c) throws RemoteException;
     void useMarker(Color color, int playerId) throws RemoteException;


}
