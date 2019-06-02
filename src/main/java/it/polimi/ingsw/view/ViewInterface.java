package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

public interface ViewInterface {

    void sendUpdates(UpdateClass update);

    //STARTGAME to switch gui to the main game window from lobby
    void startGame();

    //SPAWN
    void startSpawn();
    void spawn(CachedPowerUp powerUp);

    //POWERUP
    void startPowerUp();
    void askGrenade();

    //ACTION
    void startAction();
    void useMarker(Color color, int playerId);
    Boolean askMoveValid(int row, int column, Directions direction);

    //RELOAD
    void startReload();

    /**
     *  used for actions submitted by the client
     * @param jsonAction is the action that the clients want to do
     */
    void doAction(JsonAction jsonAction);

}
