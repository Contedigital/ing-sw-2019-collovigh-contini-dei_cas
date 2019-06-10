package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Weapon {

    private Player firstTarget;//first target of every shot , useful for checking  if we need to retarget players or similar

    /**
     * Abstract class, no constructor
     */
    public Weapon(){}
    /**
     * @return Boolean if the weapon is loaded
     */
    public abstract boolean isLoaded();

    /**
     * reloads the weapon
     */
    public abstract void reload() throws NotAbleToReloadException ;

    /**
     * every weapon type need to say if it can be reloaded in its own way
     *
     * @return true if the weapon can be reloaded
     */
    public abstract boolean canBeReloaded();
    public Player getFirstTarget() {
        return firstTarget;
    }//useful when you have to target  different target
    public void setFirstTarget(Player firstTarget) {
        this.firstTarget = firstTarget;
    }

    public final Player isPossessedBy(){

        List<Player> list = Model.getGame().getPlayers().stream()
                .filter(player -> player.getCurrentWeapons().hasItem(this))
                .collect(Collectors.toList());

        return (list.isEmpty()) ? null : list.get(0);
    }

    /**
     * this method is the method that leads all weapons to the shooting action
     * a list of itnegers who enumbers the effects (0 to 3) for every effects you have a list of targets
     * cells because you may move a target with move microEffects
     * @param targetLists
     * @param effects
     * @param cells
     * @throws WeaponNotLoadedException
     * @throws OverKilledPlayerException
     * @throws DeadPlayerException
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws FrenzyActivatedException
     */
    public abstract void shoot(ArrayList<ArrayList<Player>> targetLists, ArrayList<Integer> effects, ArrayList<Cell> cells) throws WeaponNotLoadedException, OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, FrenzyActivatedException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException;//may need to be changed

    /**
     * @param cost
     * @param possessed
     * @return true if the player can pay something in ammo
     */
    public final boolean canPay(List<AmmoCube> cost, AmmoBag possessed) {

        List<Color> cash = possessed
                .getList()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        List<Color> required = cost
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());


        return cash.containsAll(required);
    }

    /**
     * print some infos about weapons--useful for client infos screening
     */
    public abstract void print();

    /**
     * name of the ammo
     * @return
     */
    public abstract String getName();

    public abstract List<AmmoCube> getCost();

}
