package it.polimi.ingsw.model;

import customsexceptions.CardNotPossessedException;

import java.util.List;

public interface Bag <T> {

     List<T> getList();

     void addItem(T item);

     T getItem( T item) throws CardNotPossessedException;

     Boolean hasItem( T item);


}
