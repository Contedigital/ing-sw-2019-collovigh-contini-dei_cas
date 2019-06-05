package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;

public class JsonCell {

    private CellType cellType;
    private CellColor color;
    private boolean visit;
    private boolean isAmmoCell;
    private Point adjNorth;
    private Point adjSouth;
    private Point adjEast;
    private Point adjWest;
    private List<Player> playersHere;

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public void setColor(CellColor color) {
        this.color = color;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
    }

    public void setAmmoCell(boolean ammoCell) {
        isAmmoCell = ammoCell;
    }

    public void setAdjNorth(Point adjNorth) {
        this.adjNorth = adjNorth;
    }

    public void setAdjSouth(Point adjSouth) {
        this.adjSouth = adjSouth;
    }

    public void setAdjEast(Point adjEast) {
        this.adjEast = adjEast;
    }

    public void setAdjWest(Point adjWest) {
        this.adjWest = adjWest;
    }

    public void setPlayersHere(List<Player> playersHere) {
        this.playersHere = playersHere;
    }

    public CellType getCellType() {
        return cellType;
    }

    public CellColor getColor() {
        return color;
    }

    public boolean isVisit() {
        return visit;
    }

    public boolean isAmmoCell() {
        return isAmmoCell;
    }

    public Point getAdjNorth() {
        return adjNorth;
    }

    public Point getAdjSouth() {
        return adjSouth;
    }

    public Point getAdjEast() {
        return adjEast;
    }

    public Point getAdjWest() {
        return adjWest;
    }

    public List<Player> getPlayersHere() {
        return playersHere;
    }
}
