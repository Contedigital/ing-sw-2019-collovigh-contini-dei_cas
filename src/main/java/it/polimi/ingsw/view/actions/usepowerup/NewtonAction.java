package it.polimi.ingsw.view.actions.usepowerup;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;

public class NewtonAction extends PowerUpAction {

    private final int targetPlayerId;
    private final int amount;
    private final Directions direction;


    public NewtonAction(Color color, int targetPlayerId, int amount, Directions direction) {
        super(color, PowerUpType.NEWTON);
        this.targetPlayerId = targetPlayerId;
        this.amount = amount;
        this.direction = direction;
    }

    public int getTargetPlayerId() {
        return targetPlayerId;
    }

    public int getAmount() {
        return amount;
    }

    public Directions getDirection() {
        return direction;
    }
}
