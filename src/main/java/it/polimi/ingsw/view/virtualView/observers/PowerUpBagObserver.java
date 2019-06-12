package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.player.PowerUpBag;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.ArrayList;
import java.util.List;

public class PowerUpBagObserver implements Observer {

    private final PlayerObserver playerObserver;

    public PowerUpBagObserver(PlayerObserver up) {

        this.playerObserver = up;

    }

    @Override
    public void update(Object object) {

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedPowerUpBag(extractCachedPowerUp((PowerUpBag) object),playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerObserver.getPlayerId())
                .sendUpdates(updateClass);
    }


    @Override
    public void updateSinge(int playerId, Object object) {

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedPowerUpBag(extractCachedPowerUp((PowerUpBag) object),playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }

    private List<CachedPowerUp> extractCachedPowerUp(PowerUpBag powerUpBag){

        List<CachedPowerUp> powerUpList = new ArrayList<>();

        for (PowerUp up: powerUpBag.getList()){

            powerUpList.add(new CachedPowerUp(up.getType(),up.getColor()));
        }

        return powerUpList;
    }


}
