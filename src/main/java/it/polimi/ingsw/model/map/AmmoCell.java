package it.polimi.ingsw.model.map;

import it.polimi.ingsw.controller.saveutils.SavedCell;
import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent the ammo cell of the map
 */
public class AmmoCell extends Cell {

    /**
     * is the ammoCard placed in the cell
     * @see it.polimi.ingsw.model.ammo.AmmoCard
     */
    private AmmoCard ammoPlaced;

    /**
     * Default constructor
     */
    public AmmoCell() {

        super();

        // Place the AmmoCard

        ammoPlaced = AmmoCard.generateRandCard();

        // Sets the Observer

        if (Observers.isInitialized()) this.addObserver(Observers.getAmmoCellObserver());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isAmmoCell() {
        return true;
    }

    /**
     * This constructor is used to return a copy of the AmmoCell passed as a parameter
     * @param clone AmmoCell to be cloned
     */
    public AmmoCell(AmmoCell clone){

        super(clone);
        this.ammoPlaced = clone.ammoPlaced;
    }


    /**
     * @return a randomly generated Ammo Card, based on the probability of the real on-board game deck
     * the ammoPlaced is generated randomly inside the creator so that the ammo is the same for all players and is not
     * generated randomly every time a Player wants to check what Ammo is placed inside this Cell
     */
    @Override
    public AmmoCard getAmmoPlaced() {

        return ammoPlaced;
    }

    /**
     * @return the Ammo picked up by Player inside this Cell, also it generates a new PowerUp to be placed inside Cell
     */
    @Override
    public AmmoCard pickAmmoPlaced() {

        AmmoCard tempAmmo = ammoPlaced;

        ammoPlaced = null;

        updateAll(this);

        return tempAmmo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Weapon> getWeapons() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedCell getSaveVersionOfCell() {

        return new SavedCell(this);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateAmmoCard(){

        if (ammoPlaced == null) ammoPlaced = AmmoCard.generateRandCard();

        updateAll(this);

    }

    /**
     * This method will be used only for save purposes
     * @param ammoCard is the ammoCard to set
     */
    public void setAmmoPlaced(AmmoCard ammoCard){

        this.ammoPlaced = ammoCard;
    }


}
