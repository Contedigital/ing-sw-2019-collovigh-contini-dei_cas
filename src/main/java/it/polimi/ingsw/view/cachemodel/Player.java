package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;

public class Player {

    private final int playerId;



    private final PlayerColor playerColor;
    private final String name;
    private CachedStats stats;
    private CachedPowerUpBag powerUpBag;
    private CachedWeaponBag weaponbag;
    private CachedAmmoBag ammoBag;

    public Player(int playerId, String name, PlayerColor color) {
        this.playerId = playerId;
        this.name = name;
        this.playerColor = color;
    }

    public void update(CachedStats stats){

        this.stats = stats;
    }

    public void update(CachedPowerUpBag powerUpBag){

        this.powerUpBag = powerUpBag;
    }

    public void update(CachedAmmoBag ammoBag){

        this.ammoBag = ammoBag;
    }

    public void update(CachedWeaponBag weaponbag){

        this.weaponbag = weaponbag;
    }

    public CachedStats getStats() {
        return stats;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public CachedPowerUpBag getPowerUpBag() {
        return powerUpBag;
    }

    public CachedAmmoBag getAmmoBag() {
        return ammoBag;
    }

    public CachedWeaponBag getWeaponbag() {
        return weaponbag;
    }
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    @Override
    public String toString(){

        String s;

        switch (playerColor){

            case GREY:
                s = ANSI_WHITE.escape();
                break;

            case BLUE:
                s = ANSI_BLUE.escape();
                break;

            case YELLOW:
                s = ANSI_YELLOW.escape();
                break;

            case GREEN:
                s = ANSI_GREEN.escape();
                break;

            case PURPLE:
                s = ANSI_PURPLE.escape();
                break;

            default:
                s = "";
                break;
        }

        if(getStats() != null && getWeaponbag() != null){
            return s + "ID: " + getStats().getPlayerId() + "\n" +
                    "Nome: " + getName() + "\n" +
                    "Danni: " + getStats().getDmgTaken() + "\n" +
                    "Marchi: " + getStats().getMarks() + "\n" +
                    "Online: " + getStats().getOnline() + "\n" +
                    "Armi: " + "\t" + getWeaponbag().toString() + "\n" + ANSI_RESET.escape();

        } else if(getStats() != null) {
            return s + "ID: " + playerId + "\n" +
                    "Nome: " + getName() + "\n" +
                    "Danni: " + getStats().getDmgTaken() + "\n" +
                    "Marchi: " + getStats().getMarks() + "\n" +
                    "Online: " + getStats().getOnline() + "\n" +
                    "Armi : " + " nessuna \n" + ANSI_RESET.escape();

        } else if(getWeaponbag() != null){
            return s + "ID: " + playerId + "\n" +
                    "Nome: " + getName() + "\n" +
                    //"Danni: " + getStats().getDmgTaken() + "\n" +
                    //"Marchi: " + getStats().getMarks() + "\n" +
                    //"Online: " + getStats().getOnline() + "\n" +
                    "Armi : " + "\t" + getWeaponbag().toString() + " \n" + ANSI_RESET.escape();
        }
        else {
            return s + "ID: " + playerId + "\n" +
                    "Nome: " + getName() + "\n" +
                    "Danni" + "[ ]" + "\n" +
                    "Marchi" + "[ ]" + "\n" +
                    "Online: " + " true" + "\n" +
                    "Armi : " + " nessuna \n" + ANSI_RESET.escape();
        }
    }
}
