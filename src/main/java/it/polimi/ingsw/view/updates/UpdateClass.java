package it.polimi.ingsw.view.updates;


import java.io.Serializable;

public abstract class UpdateClass implements Serializable {

    private final UpdateType type;
    private final Update update;
    private final int playerId;



    public UpdateClass(UpdateType type, Update update, int playerId) {
        this.type = type;
        this.update = update;
        this.playerId = playerId;
    }

    public UpdateClass(UpdateType type, Update update) {
        this.type = type;
        this.update = update;
        this.playerId = -1;
    }

    public Update getUpdate() {
        return update;
    }

    public UpdateType getType() {
        return type;
    }

    public int getPlayerId() {
        return playerId;
    }
}
