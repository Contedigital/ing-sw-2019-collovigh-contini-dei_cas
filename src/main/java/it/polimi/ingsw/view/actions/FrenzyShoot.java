package it.polimi.ingsw.view.actions;

import java.awt.*;
import java.util.ArrayList;
public class FrenzyShoot extends JsonAction {

    private final Move moveAction;
    private final ReloadAction reloadAction;
    private final ShootAction shootAction;


    public FrenzyShoot(Move moveAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.moveAction = moveAction;

        this.reloadAction = null;
        this.shootAction = null;
    }

    public FrenzyShoot(ReloadAction reloadAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.reloadAction = reloadAction;

        this.moveAction = null;
        this.shootAction = null;
    }

    public FrenzyShoot(ShootAction shootAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.shootAction = shootAction;

        this.reloadAction = null;
        this.moveAction = null;
    }

    /**
     * Constructor to be used in Controller
     * @param moveAction is the move component of the action
     * @param reloadAction is the reload component of the action
     * @param shootAction is the shoot component of the action
     */
    public FrenzyShoot( Move moveAction, ReloadAction reloadAction, ShootAction shootAction) {

        super(ActionTypes.FRENZY_SHOOT);

        this.moveAction = moveAction;
        this.reloadAction = reloadAction;
        this.shootAction = shootAction;
    }

    public static FrenzyShoot genFrenzyShootActionSkipShoot(){

        return new FrenzyShoot(null,null, new ShootAction(null,null, null,null,null,null,null));
    }

    public static FrenzyShoot genFrenzyShootActionSkipMove(){

        return new FrenzyShoot(new Move(new ArrayList<>(), null),null, null);
    }

    public static FrenzyShoot genFrenzyShootActionSkipReload(){

        return new FrenzyShoot(null,new ReloadAction(new ArrayList<>(),new ArrayList<>()), null);
    }

    public static FrenzyShoot genEmptyFrenzyShootAction(){

        return new FrenzyShoot(null,null, null);
    }

    public Move getMoveAction() {
        return moveAction;
    }

    public ReloadAction getReloadAction() {
        return reloadAction;
    }

    public ShootAction getShootAction() {
        return shootAction;
    }

    /**
     * This method will say which of three fields are null
     * @return -1 if none, 1 if the move action, 2 for the reload action, 3 for the shoot action
     */
    public Integer getFieldsNonNull(){

        if (shootAction != null) return 3;

        if (reloadAction != null) return 2;

        if (moveAction != null) return 1;

        return -1;
    }
}
