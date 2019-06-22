package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Thor extends SpecialWeapons {

    private static final String THOR = "T.H.O.R.";

    private ArrayList<AmmoCube> weaponCost;//cost of the weapon
    private ArrayList<AmmoCube> effectsCost;//only this beacuse every effects cst 1 ammoCube so get(0)->second effect cost and get(1) is the second
    private boolean isLoaded;

    public  ArrayList<AmmoCube> getEffectsCost()
    {
        return this.effectsCost;
    }
    public Thor() {

        super(THOR);

        weaponCost=new ArrayList<>();
        effectsCost=new ArrayList<>();
        this.weaponCost.add(new AmmoCube(Color.BLUE));
        this.weaponCost.add(new AmmoCube(Color.RED));
        effectsCost.add(new AmmoCube(Color.BLUE));
        effectsCost.add(new AmmoCube(Color.BLUE));
        this.isLoaded = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, CellNonExistentException {

        for(int i=0;i<effects.size();i++)//checks that i can actually shoot
        {
            if(i==0)
            {
                if(!isPossessedBy().canSee().contains(targetLists.get(i).get(0)))
                    throw new PlayerNotSeeableException();
            }
            if(i>0)
            {

                if(!targetLists.get(i-1).get(0).canSee().contains(targetLists.get(i).get(0)))
                    throw new PlayerNotSeeableException();

            }

            if(effects.get(i)!=i)
                throw new UncorrectEffectsException();
        }

        for(int i=0;i<effects.size();i++)
        {

            if(i==0)//first macroeffect
            {
                targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(),2);
            }else if(i==1)//second macroeffect
            {
                targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(),1);
            }
            else if(i==2)//third macroeffect
            {
                targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(),2);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {

        return this.weaponCost;

    }

    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        throw new UnsupportedOperationException();
    }
}
