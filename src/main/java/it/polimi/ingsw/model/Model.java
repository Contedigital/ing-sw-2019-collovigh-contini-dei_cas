package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Model {

    /**
     * Default constructor
     */
    public Model(List<String> playerNames, List<PlayerColor> playerColors, int mapType) {

        Map map = new Map();

        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            playerList.add(new Player(playerNames.get(i), i, playerColors.get(i)));
        }

        game = new CurrentGame(playerList, map);


        map.genMap(mapType);
        map.generateCells(mapType);
    }

    private static CurrentGame game;

    public static CurrentGame getGame() {
        return game;
    }

    public static Map getMap() {
        return getGame().getMap();
    }

    public static Player getPlayer(int playerId){
        return getGame().getPlayers().get(playerId);
    }
}