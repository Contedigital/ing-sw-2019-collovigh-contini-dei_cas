package it.polimi.ingsw.model;

public class MoveShooter extends MicroEffect {

    private int cellNumber;//the shooter can move before or after shooting with some weapons
    public MoveShooter(int n){
        this.cellNumber=n;

    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }

    public int getMoves()
    {
        return this.cellNumber;
    }
    public void setMoves(int n)
    {
        this.cellNumber=n;
    }
}
