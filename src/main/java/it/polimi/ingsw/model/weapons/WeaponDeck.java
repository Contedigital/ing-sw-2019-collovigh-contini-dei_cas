package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Deck;

import java.util.*;

/**
 *
 */
public class WeaponDeck implements Deck<Weapon> {

    private List<Weapon> weaponList;
    private Random random;


    /**
     * Default constructor
     */
    private WeaponDeck() {

        this.weaponList = new ArrayList<>();
        weaponList.addAll(NormalWeapon.getNormalWeapons());
        //then add special weapons
    }


    /**
     * This constructor is used to generate a copy instance of WeaponDeck
     * @param clone WeaponDeck instance to be copied
     */
    public WeaponDeck(WeaponDeck clone){ this.weaponList = new ArrayList<>(clone.weaponList); }


    /**
     * this function is thought to be called jut once at the beginning of the game
     *@return a new instance of a WeaponDeck which is full
     */
    public static WeaponDeck populateDeck(){
        WeaponDeck deck = new WeaponDeck();
        return deck;
    }


    /**
     *
     * @return a pointer to a Weapon which will no longer be in the Deck
     */
    public Weapon getRandomCard() {

        if (this.weaponList == null){ throw new NullPointerException();}

        else{

            if(this.weaponList.isEmpty()){
                return null;
            }

            random = new Random();
            int i = random.nextInt(this.weaponList.size());

            Weapon weapon = this.weaponList.get(i);

            this.weaponList.remove(i);

            return weapon;
        }
    }

    @Override
    public int getSize() {
        return this.weaponList.size();
    }

}