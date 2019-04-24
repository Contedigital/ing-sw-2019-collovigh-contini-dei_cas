package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Damage;
import it.polimi.ingsw.model.Marker;
import org.junit.jupiter.api.Test;

class MacroEffectTest {

    @Test
    void effectCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();//create the MicroEffects Array
        MacroEffect.effectCreator();
        for(int i=0;i<MacroEffect.getMacroEffects().size();i++)//just for seeing something
        {
            System.out.println(MacroEffect.getMacroEffects().get(i).getName());
            System.out.println(MacroEffect.getMacroEffects().get(i).getClass());
        }

    }
}