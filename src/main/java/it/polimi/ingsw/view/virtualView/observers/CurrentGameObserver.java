package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedGame;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class CurrentGameObserver implements Observer {

    private CachedGame game;
    private Observers observers;

    public CurrentGameObserver() {
    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.game = (CachedGame) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.GAME, game, -1);

        // send the update to all Virtual Views

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
