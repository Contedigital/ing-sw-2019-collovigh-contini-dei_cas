package it.polimi.ingsw;

import java.util.*;

/**
 *  This class will be used to instantiate two decks of power ups one from which player draw cards and one that collect all discarded cards
 */
public class PowerUpDeck implements Deck<PowerUp> {

    private List<PowerUp> powerUpList;
    private Random random;

    /**
     * Constructor: it just initialize the list as a CopyOnWriteArrayList
     */
    public PowerUpDeck() {

        this.powerUpList = new ArrayList<>();
    }

    /**
     *
     * this function is thought to be called jut once at the beginning of the game
     * @return a new instance of a PowerUpDeck which is full
     */
    public static PowerUpDeck populatedDeck(){

        PowerUpDeck deck = new PowerUpDeck();


        EnumSet.allOf(Color.class)
                .forEach(color -> {

                    for (int i = 0; i <2 ; i++) {

                        deck.reinsert(new KineticRay(color));

                    }
                });

        EnumSet.allOf(Color.class)
                .forEach(color -> {

                    for (int i = 0; i <2 ; i++) {

                        deck.reinsert(new Viewfinder(color));

                    }
                });

        EnumSet.allOf(Color.class)
                .forEach(color -> {

                    for (int i = 0; i <2 ; i++) {

                        deck.reinsert(new Teleport(color));

                    }
                });

        EnumSet.allOf(Color.class)
                .forEach(color -> {

                    for (int i = 0; i <2 ; i++) {

                        deck.reinsert(new VenomGranade(color));

                    }
                });


        return deck ;

    }


    /**
     * This method is meant to be used to build the discarded card's deck:
     * the discarded card will be added to the aforementioned deck which will replace the main one once it will be empty,
     * there could have been just one deck that will be regenerated once empty,
     * but this way we can keep track also of the card in players hand.
     *
     * @param p the powerUp will be reinserted in the deck
     */
    public synchronized void reinsert(PowerUp p) {

        this.powerUpList.add(p);

    }


    /**
     *
     * @return a pointer to a PowerUp which will no longer be in the Deck
     */
    @Override
    public synchronized PowerUp getRandomCard() {
        if (this.powerUpList == null){ throw new NullPointerException();}

        else{

            if(this.powerUpList.isEmpty()){
                return null;
            }

            random = new Random();
            int i = random.nextInt(this.powerUpList.size());

            PowerUp powerUp = this.powerUpList.get(i);

            this.powerUpList.remove(i);

            return powerUp;


        }

    }
}