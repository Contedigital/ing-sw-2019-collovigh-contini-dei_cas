package it.polimi.ingsw.model;

/**
 * This class is an interface that will be implemented by AmmoDeck and PowerUpDeck
 */
public interface Deck<T> {

    /**
     * this method is meant to draw a card from the deck casually, meaning that the decks will never be shuffled
     */
    public T getRandomCard();

    /**
     *
     * @return integer representing the size of the deck
     */
    public int getSize();

}