package it.polimi.ingsw.model;

import it.polimi.ingsw.model.AmmoCube;
import it.polimi.ingsw.model.Bag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AmmoBag implements Bag<AmmoCube> {

    /**
     * this is an ArrayList of AmmoCubes sorted by color
     */
    private List<AmmoCube> ammoCubes;

    private static final int MAX_FOR_COLOR = 3;

    /**
     * Constructor
     */
    public AmmoBag() {

        this.ammoCubes = new ArrayList<>();
    }

    /**
     *
     * @return a copy of the list of AmmoCubes
     */
    @Override
    public List<AmmoCube> getList() {
        return new ArrayList<>(this.ammoCubes);
    }

    /**
     * This function will add an element to the list (only if the list has less than MAX_FOR_COLOR ammo cubes) and sort it
     *
     * @param item is the AmmoCubes to add
     */
    @Override
    public void addItem(AmmoCube item) {

        List<AmmoCube> list = this.ammoCubes.stream()
                .filter(ammoCube ->
                    ammoCube.getColor() == item.getColor()
                ).collect(Collectors.toList());

        if (list.size() < MAX_FOR_COLOR) {

            this.ammoCubes.add(item);

            this.ammoCubes.sort(Comparator.comparing(AmmoCube::getColor));
        }

    }

    /**
     *
     * @param item is the AmmoCube that will be given as
     * @return and will be removed from the inventory
     * Than the method will sort again the list
     */
    @Override
    public AmmoCube getItem(AmmoCube item) {

        if (this.ammoCubes.isEmpty()) return null;
        if (!this.ammoCubes.contains(item)) return null;

        this.ammoCubes.remove(item);
        this.ammoCubes.sort(Comparator.comparing(AmmoCube::getColor));
        return item;
    }

    /**
     * This method checks if the
     * @param item is held by the player and
     * @return true if this is the case
     */
    @Override
    public Boolean hasItem(AmmoCube item) {

        return this.ammoCubes.contains(item);
    }
}
