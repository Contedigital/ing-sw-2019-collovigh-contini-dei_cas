package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.Server;

import java.util.logging.Logger;

public class GameManager {

    public static void main(String[] args) {

        Logger LOGGER = Logger.getLogger("infoLogging");

        Controller controller = new Controller();
        Server server = new Server(controller);

    }

}
