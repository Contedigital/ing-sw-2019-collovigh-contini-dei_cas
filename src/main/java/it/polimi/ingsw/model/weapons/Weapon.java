package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Weapon implements Serializable {

    private List<Integer> firstTarget;//first target of every shot , useful for checking  if we need to retarget players or similar

    private boolean loaded = true;

    /**
     * Abstract class, no constructor
     */
    public Weapon(){}
    /**
     * @return Boolean if the weapon is loaded
     */
    public boolean isLoaded(){
        return loaded;
    }

    /**
     * @param loaded is a boolean representing if the weapon is loaded
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * reloads the weapon
     */
    public void reload() throws NotAbleToReloadException{

        // checks if the player who owns the weapon can pay the reload

        if (!this.isPossessedBy().canPay(this.getReloadCost())) throw new NotAbleToReloadException();

        else {

            try {

                // the player pays

                this.isPossessedBy().pay(this.getReloadCost());

                // the weapon is set to loaded

                loaded = true;

            }catch (CardNotPossessedException e){

                throw new NotAbleToReloadException();
            }
        }
    }



    public ArrayList<Player> getFirstTargets() {
        ArrayList <Player>t=new ArrayList();
        for(int i:firstTarget)
        {
            t.add(Model.getPlayer(i));
        }
        return t;
    }//useful when you have to target  different target
    public void setFirstTarget(List<Player> f) {
        firstTarget=new ArrayList<>();
        for(Player item:f)
        {
            firstTarget.add(item.getPlayerId());
        }
    }

    public void removeFromFirstTargets(Player p)
    {
        for(int i=0;i<firstTarget.size();i++)
        {
            if(firstTarget.get(i)==p.getPlayerId())
                firstTarget.remove(i);
        }

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
    public abstract void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException, PrecedentPlayerNeededException;//may need to be changed

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

    public abstract List<AmmoCube> getReloadCost();

}
