package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Weapon {
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
    private Player firstTarget;//first target of every shot , useful for checking  if we need to retarget players or similar
    public Player getFirstTarget() {
        return firstTarget;
    }
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
     *
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
    public abstract void shoot(ArrayList<ArrayList<Player>> targetLists, ArrayList<Integer> effects, ArrayList<Cell> cells)throws WeaponNotLoadedException, OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, FrenzyActivatedException;//may need to be changed

    /**
     *
     * @param cost
     * @param possessed
     * @return true if the player can pay something in ammo
     */
    public final boolean canPay(List<AmmoCube> cost, AmmoBag possessed)
    {
        int []ammo=possessed.getAmount();
        int[]ammoC=new int[3];
        for(int i=0;i<cost.size();i++)
        {
            if(cost.get(i).getColor()== Color.RED)
            {
                ammoC[0]++;
            }else if(cost.get(i).getColor()==Color.BLUE)
            {
                ammoC[1]++;
            }else{
                ammoC[2]++;
            }
        }
        if(ammo[0]-ammoC[0]>=0&&ammo[1]-ammoC[1]>=0&&ammo[2]-ammoC[2]>=0)//ammo i Have - ammo Cost for each type il >=0 then i can reload
        {
            return true;
        }else{
            return false;
        }
    }

    public abstract void print();


}
