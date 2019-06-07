package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CachedAmmoCell extends CachedCell {

    private final List<Color> ammoList;
    private final Boolean powerUp;

    public CachedAmmoCell(List<Color> ammoList, Boolean powerUp, Point position ) {

        super(CellType.AMMO, position);

        this.ammoList = ammoList;

        this.powerUp = powerUp;

    }

    public List<Color> getAmmoList() {
        return ammoList;
    }

    public Boolean hasPowerUp() {
        return powerUp;
    }
}
