package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.Mover;
import it.polimi.ingsw.model.weapons.Damage;
import it.polimi.ingsw.model.weapons.MacroEffect;
import it.polimi.ingsw.model.weapons.Marker;
import it.polimi.ingsw.model.weapons.NormalWeapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

class NormalWeaponTest {

    @Test
    void weaponsCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        NormalWeapon.weaponsCreator();
        assertNotNull(NormalWeapon.getNormalWeapons());

    }

    @Test
    void shoot() throws FrenzyActivatedException {//one shot, one effect, one target, free effect---the most basic possible
        weaponsCreator();

        ArrayList<String> playerNames=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("target");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2);


        //generate a player with a name and its starting position
        //Player p1 = new Player("Shooter",map.getCell(1,3));
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(1,3));

        //Player p2 = new Player("Visible",map.getCell(0,3));
        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,3));


        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        ArrayList targets=new ArrayList();
        targets.add(target1);
        ArrayList<ArrayList<Player>>targetsLists=new ArrayList<>();
        targetsLists.add(targets);
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy

        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            shooter.getWeapons().get(0).shoot(targetsLists,mEf,null);
            //System.out.println(target1.getStats().getDmgTaken());
        }
        catch(WeaponNotLoadedException e){ e.printStackTrace();} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot2() throws FrenzyActivatedException{//now two effects ,you also need to pay and the player can, if it can't the notEnoughAmmoException sclera
        weaponsCreator();// 2 damages and 1 mark

        ArrayList<String> playerNames=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("target1");
        playerNames.add("target2");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2);


        //generate a player with a name and its starting position
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,0));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        ArrayList targets0=new ArrayList();
        ArrayList targets1=new ArrayList();
        ArrayList<ArrayList<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets1.add(target2);
        targetLists.add(targets0);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();
        System.out.println(shooter.canSee().get(0).getPlayerName());
        System.out.println(shooter.canSee().get(1).getPlayerName());//------------canSee smatta, perchè??


        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(1));//costs 1 red AmmoCube
            shooter.getWeapons().get(0).shoot(targetLists,mEf,null);}

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot3() throws FrenzyActivatedException{//same as the shoot2 test but launches the notEnoughAmmoException
        weaponsCreator();

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        ArrayList targets=new ArrayList();
        targets.add(target1);
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(1));//costs 1 red AmmoCube




            shooter.getWeapons().get(0).shoot(targets,mEf,null);

        }
        catch(WeaponNotLoadedException e){} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot4() throws FrenzyActivatedException{//Shoot, 2 effects and mover effect taht moves the target in the shooter cell
        weaponsCreator();

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));

        target1.setPlayerPos(Model.getMap().getCell(1,3));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        ArrayList targets=new ArrayList();
        targets.add(target1);
        try{


            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(5).getEffects().get(0));//tractor beam
            mEf.add(NormalWeapon.getNormalWeapons().get(5).getEffects().get(1));//costs 1 red and 1 yellow AmmoCube
            NormalWeapon.getNormalWeapons().get(5).enableMoveBefore();


            shooter.getWeapons().get(0).shoot(targets,mEf,target1.getCurrentPosition());
            assertEquals(shooter.getCurrentPosition(),target1.getCurrentPosition());
        }
        catch(WeaponNotLoadedException e){} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }
}