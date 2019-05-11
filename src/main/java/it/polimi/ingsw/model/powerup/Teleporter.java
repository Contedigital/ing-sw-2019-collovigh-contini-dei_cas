package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;

/**
 * 
 */
public class Teleporter extends PowerUp {

    /**
     * Default constructor
     */
    public Teleporter(Color color) {

        super(color);
        this.setType(PowerUpType.TELEPORTER);
    }

    /**
     *
     * @param cell cell in which the player will be teleported
     */
    public void use(Cell cell) throws CardNotPossessedException, CellNonExistentException {

        Boolean b = false;
        Player p = null;

        for (Player player: Model.getGame().getPlayers()){

            if (player.getPowerUpBag().hasItem(this)) {
                b = true;
                p=player;
            }

        }

        if(!b){ throw new CardNotPossessedException();}
        else{

            if (cell == null) {throw new CellNonExistentException();}

            else p.setPlayerPos(cell);
        }


    }

}