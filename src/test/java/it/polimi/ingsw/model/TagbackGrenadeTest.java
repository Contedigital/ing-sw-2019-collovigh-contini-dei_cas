package it.polimi.ingsw.model;

import customsexceptions.CardNotPossessedException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagbackGrenadeTest {

    @Test
    void applyOn() {

        List<String> names = new ArrayList<>();

        names.add("Frank");
        names.add("Alex");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.PURPLE);



        Model kat = new Model(names,colors,2);

        TagbackGrenade grenade = new TagbackGrenade(Color.BLUE);

        assertThrows(CardNotPossessedException.class,() -> {
            grenade.applyOn(Model.getPlayer(1));
        });


        Model.getPlayer(0).addPowerUp(grenade);

        try {
            grenade.applyOn(Model.getPlayer(1));
        }catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(0,Model.getGame().getPlayers().get(1).getStats().getMarks().get(0));
        assertEquals(1,Model.getGame().getPlayers().get(1).getMarks().size());
    }
}