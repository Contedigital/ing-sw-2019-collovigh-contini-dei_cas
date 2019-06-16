package it.polimi.ingsw.controller;


import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.actions.ReloadAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.DefaultReplies.*;

public class ReloadPhase {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static final Level level = Level.INFO;

    private static final String LOG_START = "[Controller-ReloadPhase]";

    private final Controller controller;

    public ReloadPhase(Controller controller) {

        this.controller = controller;

    }

    /**
     * This method will ask the view if the player wants to reload his weapon if he can
     */
    public void handleReload(){

        // gets the current player

        int playerId = controller.getCurrentPlayer();

        if((!(Model.getGame().getPlayers().get(playerId).getWeapons().isEmpty())) && (playerHasReloadPhase())){

            controller.getVirtualView(playerId).startReload();

        }else {

            LOGGER.log(level,()->LOG_START + " player w/ id:  " + playerId + " skipped reload bc has no weapons ");

            // if the player has no weapon the phase get skipped automatically

            controller.incrementPhase();
        }
    }

    /**
     *
     * @param reloadAction is the json action from the client
     */
    public void reload(ReloadAction reloadAction){

        if (checkIfReloadIsValid(reloadAction)){

            // gets current player id

            int playerId = controller.getCurrentPlayer();

            // sells the specified powerUps

            try{

                List<PowerUp> toDiscard = getSpecifiedPowerUp(reloadAction.getPowerUps());

                for (PowerUp powerUp: toDiscard){

                    Model.getPlayer(playerId).sellPowerUp(powerUp);

                }

            }catch (CardNotPossessedException e){

                LOGGER.log(Level.WARNING,e.getMessage(),e);
            }

            // actually reload all the weapons

            try {

                for (Weapon weapon : getWeapons(reloadAction.getWeapons())){

                    weapon.reload();
                }

            }catch(Exception e){

                LOGGER.log(Level.WARNING, () -> LOG_START +" [CRITICAL] player passed all controls for reload but finally could not do it ");
            }

            // increment phase

            controller.incrementPhase();

        }else {

            LOGGER.log(Level.WARNING, () -> LOG_START + "  player failed controls for reload ");

            handleReload();
        }

    }

    /**
     * This method will get the real weapons from their names
     * @param weaponNames is a list of string representing weapons names
     * @return a list of weapons
     * @throws WeaponNotFoundException if a weapon is not found
     */
    private List<Weapon> getWeapons(List<String> weaponNames) throws WeaponNotFoundException {

        // gets the current player

        int playerId = controller.getCurrentPlayer();

        try{

            List<Weapon> weaponList = weaponNames
                    .stream()
                    .map( name -> controller.getUtilityMethods().findWeaponInWeaponBag(name,playerId))
                    .collect(Collectors.toList());

            if (weaponList.contains(null)) throw new WeaponNotFoundException();

            return (weaponList.contains(null)) ? null : weaponList;

        }catch (Exception e){

            throw new WeaponNotFoundException();
        }
    }

    /**
     *  THis method will check if the specified powerUps belongs to the player
     * @param powerUps is a list of cachedPowerUp received from the view
     * @return true if the player effectively has them
     */
    private Boolean checkIfPlayerPossessPowerUps(List<CachedPowerUp> powerUps){

        Boolean returnValue = true;

        try{

            getSpecifiedPowerUp(powerUps);

        }catch (CardNotPossessedException e){

            // log

            LOGGER.log(Level.WARNING,() -> LOG_START + " player does not possess all powerUps he declared ");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP);

            return false;
        }

        return returnValue;
    }

    /**
     * This method will get a list of PowerUp from a list of CachedPowerUp
     * @param powerUps is a list of cachedPowerUp
     * @return a list of PowerUp
     * @throws CardNotPossessedException if a powerUp was not found
     */
    private List<PowerUp> getSpecifiedPowerUp(List<CachedPowerUp> powerUps) throws CardNotPossessedException {

        // declare a list of powerUp

        List<PowerUp> powerUpList = new ArrayList<>();

        // get a copy of the list of the powerUp possessed by the model

        List<PowerUp> possessed = Model.getPlayer(controller.getCurrentPlayer()).getPowerUpBag().getList();

        // for each cachedPowerUp gets the correspondent one from the model and adds it to the new list

        for (CachedPowerUp cachedPowerUp : powerUps){

            PowerUp toRemove = controller.getUtilityMethods().cachedToRealPowerUp(cachedPowerUp,possessed);

            powerUpList.add(toRemove);

            // then removes it from the original list so that can not be picked again

            possessed.remove(toRemove);

        }

        return powerUpList;
    }

    /**
     * This method will check if the player can pay the reload cost with the parameters he specified
     * @param ammoCubeList is a list of ammoCubes
     * @param weaponList is the list of weapon to reload
     * @return true if yes, false otherwise
     */
    private Boolean checkIfPlayerCanPayReload(List<AmmoCube> ammoCubeList, List<Weapon> weaponList){

        Boolean returnValue = true;

        List<Color> required = new ArrayList<>();

        for (Weapon weapon : weaponList){

            required.addAll(weapon
                    .getReloadCost()
            .stream()
            .map(AmmoCube::getColor)
            .collect(Collectors.toList()));

        }

        List<Color> possessed = ammoCubeList
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        if( ! possessed.containsAll(required)){

            returnValue = false;

            // log

            LOGGER.log(Level.WARNING, ()-> LOG_START + " player tried to reload but did not have enough ammo");

            // show

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_NO_ENOUGH_AMMO);
        }

        return returnValue;
    }

    /**
     * This method will check if the overall action is doable
     * @param reloadAction is the action submitted by the client
     * @return true if verified, false otherwise
     */
    private Boolean checkIfReloadIsValid(ReloadAction reloadAction){

        Boolean returnValue;

        // check if player possess the specified powerUps

        returnValue =  checkIfPlayerPossessPowerUps(reloadAction.getPowerUps());

        // gets the weapon if them are correct

        List<Weapon> weaponList;

        List<AmmoCube> possessed = new ArrayList<>();

        try{

            weaponList = getWeapons(reloadAction.getWeapons());

        }catch (WeaponNotFoundException e){

            LOGGER.log(Level.WARNING,() -> LOG_START + " player specified weapon that were not found ");

            controller.getVirtualView(controller.getCurrentPlayer()).show(DEFAULT_WEAPON_NOT_FOUND_IN_BAG);

            return false;
        }

        // gets the player Ammo + the powerUp he decided to discard

        try{

            possessed.addAll(controller.getUtilityMethods().powerUpToAmmoList(getSpecifiedPowerUp(reloadAction.getPowerUps())));

            possessed.addAll(Model.getPlayer(controller.getCurrentPlayer()).getAmmoBag().getList());


        }catch (CardNotPossessedException e){

            LOGGER.log( Level.WARNING, e.getMessage(),e);
        }

        // checks if he can pay the reload

        returnValue = returnValue && checkIfPlayerCanPayReload(possessed,weaponList);

        return returnValue;
    }

    /**
     * This method scan the model to find if the player can actually reload any of his weapon
     * @return true if he can
     */
    private Boolean playerHasReloadPhase(){

        // gets the current player id

        int playerId = controller.getCurrentPlayer();

        // declare a boolean var

        Boolean returnValue = false;

        // gets the whole potential ( real + converted powerUp) ammoList the player owns

        List<Color> fullAmmoList = new ArrayList<>();

        fullAmmoList.addAll(controller.getUtilityMethods().powerUpToAmmoList(Model.getPlayer(playerId).getPowerUpBag().getList()).stream().map(AmmoCube::getColor).collect(Collectors.toList()));

        fullAmmoList.addAll(Model.getPlayer(playerId).getAmmoBag().getList().stream().map(AmmoCube::getColor).collect(Collectors.toList()));

        //check if the player can reload any of his weapon

        for (Weapon weapon : Model.getPlayer(playerId).getCurrentWeapons().getList()){

            List<Color> required = weapon
                    .getReloadCost()
                    .stream()
                    .map(AmmoCube::getColor)
                    .collect(Collectors.toList());

            returnValue = returnValue || fullAmmoList.containsAll(required);

        }

        return returnValue;
    }
}
