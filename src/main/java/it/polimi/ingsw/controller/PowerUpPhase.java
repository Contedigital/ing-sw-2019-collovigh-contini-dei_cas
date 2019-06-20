package it.polimi.ingsw.controller;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.powerup.TagbackGrenade;
import it.polimi.ingsw.model.powerup.Teleporter;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.otherplayerturn.PowerUpTurnUpdate;

import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.PowerUpType.*;
import static it.polimi.ingsw.utils.DefaultReplies.*;

public class PowerUpPhase {

    // LOGGER

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private static final int TIMER_POWER_UP = 10;


    // Controller reference

    private final Controller controller;

    public PowerUpPhase(Controller controller) {
        this.controller = controller;
    }

    // Server -> Client flow

    /**
     * This methods will forward the action Request to the virtual View
     */
    public void handlePowerUp(){

        // if someone has been shot ask them to use grenades

        askGrenadeToShot();

        int currentPlayer = controller.getCurrentPlayer();

        if (!controller.isPlayerOnline(currentPlayer)){

            // if the player is not online skips the turn

            controller.incrementPhase();

        }else if(!hasPowerUpPhase()){

            //if current player hasn't got any usable PowerUp in hand  ( Newton or Teleporter ) -> skip this phase

            controller.incrementPhase();

        }else{

            // start the timer

            controller.getTimer().startTimer(TIMER_POWER_UP);

            controller.setExpectingAnswer(true);

            // sends the action

            controller.getVirtualView(currentPlayer).startPowerUp();

        }
    }

    public void askGrenadeToShot(){

        if (!controller.getShotPlayerThisTurn().isEmpty()) {

            int playerId = controller.getShotPlayerThisTurn().get(0);

            if ( ( controller.isPlayerOnline(playerId) ) && ( hasGrenade(playerId) ) ) {

                controller.getVirtualView(playerId).askGrenade();

                // start the timer

                controller.getTimer().startTimer(TIMER_POWER_UP);

                controller.setExpectingAnswer(true);

            }else {

                // if the player does not have the grenades it gets removed from the list and skipped

                controller.getShotPlayerThisTurn().remove(0);

                askGrenadeToShot();
            }

        }

    }

    // Client -> Server flow

    public void usePowerUp(PowerUpAction powerUpAction){

        // stop timer

        controller.getTimer().stopTimer();

        controller.setExpectingAnswer(false);

        switch (powerUpAction.getPowerUpType()){

            case NEWTON:

                useNewton((NewtonAction) powerUpAction);

                break;

            case TELEPORTER:

                useTeleport((TeleporterAction) powerUpAction);

                break;

            case TARGETING_SCOPE:

                break;

            case TAG_BACK_GRENADE:

                useGrenade((GrenadeAction) powerUpAction);

                break;

            default:

                break;

        }

        // notify the inactive players

        controller.updateInactivePlayers(new PowerUpTurnUpdate(controller.getCurrentPlayer(),new CachedPowerUp(powerUpAction.getPowerUpType(),powerUpAction.getColor())));
    }

    public void useNewton(NewtonAction newtonAction){

        int currentPlayer = controller.getCurrentPlayer();

        if (newtonAction.getTargetPlayerId() == currentPlayer){

            LOGGER.warning("[CONTROLLER - PowerUp] player tried to use Newton on himself");

            controller.getVirtualView(currentPlayer).show(DEFAULT_PLAYER_USED_NEWTON_ON_HIMSELF);

            handlePowerUp();
        }

        LOGGER.log(level,"[CONTROLLER - PowerUp] calling useNewton on player w/ id: {0}",currentPlayer);

        try {

            // Locate the powerUp in the model

            Newton newton = (Newton) Model.getPlayer(currentPlayer).getPowerUpBag().findItem(NEWTON, newtonAction.getColor());

            // calls the use() method in the powerUp

            newton.use(Model.getPlayer(newtonAction.getTargetPlayerId()), newtonAction.getDirection() , newtonAction.getAmount());

            // discard the powerUp

            discardPowerUp(newtonAction.getPowerUpType(),newtonAction.getColor());

            // calls again the handlePowerUp functions in case the player wants to use another

            handlePowerUp();

        } catch(Exception e){

            LOGGER.log(Level.WARNING, e.getMessage(),e);

        }
    }

    public void useTeleport(TeleporterAction teleporterAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] calling useTeleport on player w/ id: {0} ", currentPlayer );

        // Gets the new Position

        Point newPos = teleporterAction.getCell();

        // gets the chosen Cell from the model

        Cell cell = Model.getMap().getCell( newPos.y, newPos.x);

        try {

            // Locate the powerUp in the model

            Teleporter t = (Teleporter) Model.getPlayer(currentPlayer).getPowerUpBag().findItem(TELEPORTER, teleporterAction.getColor());

            // calls the use() method in the powerUp

            t.use(cell);

            // discard the powerUp

            discardPowerUp( teleporterAction.getPowerUpType(), teleporterAction.getColor());

            // calls again the handlePowerUp functions in case the player wants to use another

            handlePowerUp();


        } catch (CardNotPossessedException e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] player do not possess given powerUp ");

            // send show message

            controller.getVirtualView(currentPlayer).show(DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP);

        } catch (CellNonExistentException e){

            LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] cell does not exist ");

            // send show message

            controller.getVirtualView(currentPlayer).show(DEFAULT_CELL_NOT_EXISTENT);
        }
    }


    public void useGrenade(GrenadeAction grenadeAction){

        int currentPlayer = controller.getCurrentPlayer();

        LOGGER.log(level, "[CONTROLLER - PowerUp] player w/ id: {0} was shot and responded with a grenade", grenadeAction.getPossessorId() );

        if (grenadeAction.getColor() == null ){

            //if the player choose to not use the grenade the color will be set to null:

            // Remove the player from the shot list

            controller.getShotPlayerThisTurn().remove(0);

            // calls teh function to ask grenade to the next person in the list

            askGrenadeToShot();

        } else {

            try {

                // Locate the powerUp in the model

                TagbackGrenade grenade = (TagbackGrenade) Model.getPlayer(grenadeAction.getPossessorId()).getPowerUpBag().findItem(TAG_BACK_GRENADE, grenadeAction.getColor());

                // use the grenade on the current player

                grenade.applyOn(Model.getPlayer(currentPlayer), grenadeAction.getPossessorId());

                // discard the powerUp

                Model.getPlayer(grenadeAction.getPossessorId()).getPowerUpBag().getItem(grenade);

                // Remove the player from the shot list

                controller.getShotPlayerThisTurn().remove(0);

                // calls teh function to ask grenade to the next person in the list

                askGrenadeToShot();

            } catch (Exception e) {

                LOGGER.log(Level.WARNING, "[CONTROLLER - PowerUp] player w/ id {0} try to use a grenade but do not possess it", grenadeAction.getPossessorId());

                // send show message

                controller.getVirtualView(currentPlayer).show(DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP);

                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }

        }

    }


    // Utils

    public void discardPowerUp(PowerUpType type, Color color){

        int currentPlayer = controller.getCurrentPlayer();

        Model.getPlayer(currentPlayer).getPowerUpBag()
                .sellItem(Model.getPlayer(currentPlayer).getPowerUpBag().findItem(type, color));

    }

    private Boolean hasGrenade(int playerId){

        List<PowerUp> list = Model
                .getPlayer(playerId)
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> x.getType().equals(TAG_BACK_GRENADE))
                .collect(Collectors.toList());

        return !(list.isEmpty());
    }

    private Boolean hasPowerUpPhase(){

        List<Player> spawned = Model
                .getGame()
                .getPlayers()
                .stream()
                .filter( x -> (x.getStats().getCurrentPosition() != null ))
                .collect(Collectors.toList());

        List<PowerUp> list = Model
                .getPlayer(controller.getCurrentPlayer())
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> (x.getType().equals(TELEPORTER)) || (x.getType().equals(NEWTON)) )
                .collect(Collectors.toList());

        if (spawned.size() <= 1){

            list = list.stream()
                    .filter( x -> x.getType().equals(TELEPORTER) )
                    .collect(Collectors.toList());

        }

        return !(list.isEmpty());

    }

    public Boolean hasTargetingScope(){

        List<PowerUp> list = Model
                .getPlayer(controller.getCurrentPlayer())
                .getPowerUpBag()
                .getList()
                .stream()
                .filter(x -> x.getType().equals(TARGETING_SCOPE) )
                .collect(Collectors.toList());

        return !(list.isEmpty());
    }

}
