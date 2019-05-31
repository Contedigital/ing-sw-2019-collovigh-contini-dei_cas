package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class AmmoBagObserver implements Observer {

    private AmmoBag ammoBag;
    private final PlayerObserver playerObserver;

    public AmmoBagObserver(PlayerObserver up) {
        this.playerObserver = up;
    }


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.ammoBag = (AmmoBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedAmmoBag(ammoBag,playerObserver.getPlayerId());

        // send the update to the Virtual View

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.ammoBag = (AmmoBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedAmmoBag(ammoBag,playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }
}
