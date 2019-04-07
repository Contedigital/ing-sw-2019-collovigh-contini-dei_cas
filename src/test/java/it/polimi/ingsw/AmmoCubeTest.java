package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCubeTest {

    @Test
    void getColor() {

        for (int i = 0; i < 100; i++) {

            Random random = new Random();

            int k = random.nextInt(3);

            Color color;

            if(k==0){ color = Color.BLUE;}

            else if(k==1){ color = Color.RED; }

                else if(k==2){ color = Color.YELLOW; }

                    else{ color = null; }

            AmmoCube cube = new AmmoCube(color);


            assertTrue( (cube.getColor() == Color.BLUE) || (cube.getColor() == Color.RED) || (cube.getColor() == Color.YELLOW));


        }




    }
}