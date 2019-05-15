package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;

public class Player {

    private final int playerId;
    private final PlayerColor playerColor;
    private final String name;
    private CachedStats stats;
    private CachedPowerUpBag powerUpBag;
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
}
