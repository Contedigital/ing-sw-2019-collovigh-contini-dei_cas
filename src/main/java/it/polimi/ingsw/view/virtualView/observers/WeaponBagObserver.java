package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class WeaponBagObserver implements Observer {

    private CachedWeaponBag cachedWeaponBag;
    private final PlayerObserver playerObserver;

    public WeaponBagObserver(PlayerObserver playerObserver) {
        this.playerObserver = playerObserver;
    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.cachedWeaponBag = (CachedWeaponBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.AMMO_BAG,cachedWeaponBag, playerObserver.getPlayerId());

        // send the update to all the Virtual Views

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.cachedWeaponBag = (CachedWeaponBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.AMMO_BAG,cachedWeaponBag, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }
}
