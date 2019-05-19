package it.polimi.ingsw.network;

import it.polimi.ingsw.view.updates.UpdateClass;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToView  {

    // Update Logic

    void sendUpdate(UpdateClass update);

    // calls the same method on view Class

    /**
     * This methods starts the spawn Phase on the client ( 0)
     */
    void startSpawn();

    /**
     * This method starts the "use power Up" phase ( 1, 3, 5)
     */
    void startPowerUp();

    /**
     * This method starts the action phase on the client ( 2, 4)
     */
    void startAction();

    /**
     * This method starts the reload phase of the turn ( 6)
     */
    void startReload();

    /**
     * This method will be called on a player if he/she was shot in the previous phase and has grenades
     */
    void useGrenade();
}
