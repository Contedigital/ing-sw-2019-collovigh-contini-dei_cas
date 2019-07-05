package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.network.networkexceptions.*;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.actions.JsonAction;
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
    int joinGame(String address, String remoteName, String name, PlayerColor color) throws RemoteException, NameAlreadyTakenException, ColorAlreadyTakenException, OverMaxPlayerException, GameAlreadyStartedException;

    /**
     *
     * @param mapType is the map chosen
     * @throws RemoteException
     */
    void voteMapType(int mapType) throws RemoteException;

    Boolean ping() throws RemoteException;

    /**
     *
     * @param name is the name chosen
     * @return the id assigned to it
     */
    int reconnect(String name, String address, String remoteName) throws RemoteException, GameNonExistentException;


    //SPAWN

    /**
     *
     * @param powerUp is the powerUp to discard for spawn
     * @throws RemoteException
     */
     void spawn(CachedPowerUp powerUp) throws RemoteException;

    //POWERUP

    /**
     * This method will make the player do the actions
     * @param jsonAction is the class used to handle the actions
     * @throws RemoteException
     */
     void doAction(JsonAction jsonAction) throws RemoteException;

    /**
     * @see it.polimi.ingsw.controller.UtilityMethods#askMoveValid(int, int, Directions) ;
     */
     boolean askMoveValid(int row, int column, Directions direction) throws RemoteException;




}
