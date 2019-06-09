package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.view.actions.Move;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.ArrayList;


public class GuiMapController {

    private static Gui gui;
    private int rows=3,col=4;
    private  Button map[][]=new Button[rows][col];
    public static void setGui(Gui g) {
        gui = g;
    }
    private ArrayList <Directions> movementDirections;
    private int validMove=-1;
    @FXML
    BorderPane pane;
    @FXML
    TextArea log;
    @FXML
    GridPane innerMap;
    @FXML
    Button b00,b01,b02,b03,b10,b11,b12,b13,b20,b21,b22,b23;
    @FXML
    ImageView powerUp1,powerUp2,powerUp3;
    @FXML
    Button stopMov;

    @FXML
    public void initialize() {

    }

    public void mapCreator()
    {


        switch(gui.getView().getCacheModel().getMapType()) {
            case 1: {
                innerMap.setStyle("-fx-background-image: url('/images/Map1in.png')");//use this for sample fot he
                buttonCreator();
                 b03.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            unClickAble();
                        }
                 });
                b20.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        unClickAble();
                    }
                });

                break;
            }
            case 2: {
                buttonCreator();
                innerMap.setStyle("-fx-background-image: url('/images/Map2in.png')");//use this for sample fot he maps everytime
                b20.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        unClickAble();
                    }
                });
                break;
            }
            case 3: {
                buttonCreator();
                innerMap.setStyle("-fx-background-image: url('/images/Map3in.png')");//use this for sample fot he
                break;
            }

        }
        b02.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                spawn();
            }
        });
        b10.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                spawn();
            }
        });
        b23.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                spawn();
            }
        });
    }
    private void unClickAble()
    {
        log.appendText("\n Not in the map");
    }

    private void spawn()
    {
        log.appendText("\n spawn cell");
    }

    private void buttonCreator()
    {
        for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS) ; // allow row to grow
            rc.setFillHeight(true); // ask nodes to fill height for row

            innerMap.getRowConstraints().add(rc);
        }
        for (int colIndex = 0; colIndex < 4; colIndex++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS) ; // allow column to grow
            cc.setFillWidth(true); // ask nodes to fill space for column

            innerMap.getColumnConstraints().add(cc);
        }

        b00.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b01.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b02.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b03.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        b10.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b11.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b12.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b13.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        b20.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b21.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b22.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b23.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        map[0][0]=b00;
        map[0][1]=b01;
        map[0][2]=b02;
        map[0][3]=b03;

        map[1][0]=b10;
        map[1][1]=b11;
        map[1][2]=b12;
        map[1][3]=b13;

        map[2][0]=b20;
        map[2][1]=b21;
        map[2][2]=b22;
        map[2][3]=b23;
    }

    @FXML
    private void move()
    {

        movementDirections=new ArrayList<>();
        int x=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosX();
        int y=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosY();
        handleMovement(x,y,3,movementDirections);
    }

    private void handleMovement(int x,int y,int m,ArrayList<Directions> movementDirections)//called from move,do stuff for real
    {
        Alert a=new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Move the Pawn in an adjacent cell click STOP on the left to stop the movements\n Remembre you have "+m+" moves left");
        a.show();
        int M=m-1;
        //enable button events

        stopMov.setOnAction(new EventHandler<ActionEvent>() {//stop button
            @Override public void handle(ActionEvent e) {
                gui.getView().doAction(new Move(movementDirections,new Point(x,y)));
            }
        });

        //buttons here enable the movements in adjacent cells
        if(y<3)
        {map[x][y+1].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("EAST",x,y)) {
                    movementDirections.add(Directions.EAST);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x][y+1].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x,y+1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x<2)
        {map[x+1][y].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("SOUTH",x,y))
                {   movementDirections.add(Directions.SOUTH);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x+1][y].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x+1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(y-1>=0)
        {map[x][y-1].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("WEST",x,y)) {
                    movementDirections.add(Directions.WEST);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x][y-1].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x,y-1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x-1>=0)
        {map[x-1][y].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("NORTH",x,y)) {
                    movementDirections.add(Directions.NORTH);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x-1][y].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x-1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
    }

    private void mapEventDeleter()
    {
        for(int i=0;i<rows;i++)//reset buttons on the map to do nothing
        {
            for(int j=0;j<col;j++)
            {
                map[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                    }

                });
            }
        }
    }
    private void eventMover(int x,int y,int m)
    {
        if(m==0)
        {
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("No more movements left. Moving...");
            a.show();
            gui.getView().doAction(new Move(movementDirections,new Point(x,y)));
            return;
        }
        for(int i=0;i<rows;i++)//reset buttons on the map to do nothing
        {
            for(int j=0;j<col;j++)
            {
                map[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                    }

                });
            }
        }
        int M=m-1;
        //buttons here enable the movements in adjacent cells
        if(y<3)
        {map[x][y+1].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("EAST",x,y)) {
                    movementDirections.add(Directions.EAST);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x][y+1].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    map[x][y+1].setStyle("-fx-background-repeat: no-repeat;");
                    eventMover(x,y+1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x<2)
        {map[x+1][y].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("SOUTH",x,y))
                {   movementDirections.add(Directions.SOUTH);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x+1][y].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x+1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(y-1>=0)
        {map[x][y-1].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("WEST",x,y)) {
                    movementDirections.add(Directions.WEST);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x][y-1].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x,y-1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x-1>=0)
        {map[x-1][y].setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(moveValidator("NORTH",x,y)) {
                    movementDirections.add(Directions.NORTH);
                    map[x][y].setStyle("-fx-background-image: null");
                    map[x-1][y].setStyle(fromIDtoIMG(gui.getView().getPlayerId()));
                    eventMover(x-1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}

    }
    /**
     * takes the id returns the string of the image
     * @param id
     * @return
     */
    private String fromIDtoIMG(int id)
    {
        switch (id) {
            case 0:
                return "-fx-background-image: url('/images/player0.png')";
            case 1:
                return "-fx-background-image: url('/images/player1.png')";
            case 2:
                return "-fx-background-image: url('/images/player2.png')";
            case 3:
                return "-fx-background-image: url('/images/player3.png')";
            case 4:
                return "-fx-background-image: url('/images/player4.png')";
        }
        return null;
    }

    public void setValidMove(int validMove) {
       this.validMove=validMove;
        synchronized(this) {
            this.notifyAll();
        }
    }
    private boolean moveValidator(String dir,int x,int y)//x and y are the arrive postions of the move dir is the direction
    {
         validMove =-1;
        gui.getView().askMoveValid(x, y, Directions.valueOf(dir));
        while(validMove == -1){
            try
            {
                synchronized(this) {
                    //System.out.println("Waiting to receive validMove reply...");
                    this.wait();
                }

            } catch (InterruptedException e) {

            }

        }

        if(validMove == 1) {
            log.appendText("\n Direzione valida!");
            return true;

        }else {//validmove=0
            Alert a =new Alert(Alert.AlertType.INFORMATION);
            log.appendText("\n direzione non vlida");
            return false;
        }

    }



    @FXML
    public void loginUpdater(String name, int id, PlayerColor color)
    {
        log.appendText("\nSi è collegato: "+name+" con l'id: "+id+" ed il colore: "+color);
    }

    public void statsUpdater(int id)
    {//the player is removed from its postion before the update

        if(!gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getOnline())
        {
            log.appendText("\nIl giocatore "+id+" si è scollegato.");

            return;
        }
        log.appendText("\nUpdated stats del player: "+id);
        int r=gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosX();
        int c=gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosY();
        mapPos(r,c,id);
        stopMov.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) { }
        });


    }



    public void printLog(String s)
    {
        log.appendText("\n"+s);
    }

    public void startSpawn()
    {
        //diplaye the powerUps in the player's screen
        powerUpDisplayer();

        Alert a=new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Choose a powerUp (on the right) to discard for the swpawn location");
        a.show();
        powerUp1.setOnMouseClicked((e) -> {//eliminate the effect
            powerUp1.setImage(null);
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0));
            });
        powerUp2.setOnMouseClicked((e) -> {
            powerUp2.setImage(null);
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1));
            });
        powerUp3.setOnMouseClicked((e) -> {
            powerUp3.setImage(null);
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2));
            });
    }

    private void mapPos(int r,int c,int id)
    {
            System.out.println("riga: "+r+"colonna :"+c);
            map[r][c].setStyle(fromIDtoIMG(id));
            log.appendText("\n Placed player "+id+" in cell "+r+c);


        //eliminating the powerups effects after the beginning
        powerUp1.setOnMouseClicked((e) -> {

        });
        powerUp2.setOnMouseClicked((e) -> {

        });
        powerUp3.setOnMouseClicked((e) -> {

        });

        //afetr move i delete moving things
       mapEventDeleter();

    }


    public void powerUpDisplayer()
    {
        powerUp1.setImage(null);
        powerUp2.setImage(null);
        powerUp3.setImage(null);
        PowerUpType pt;
        ArrayList <ImageView>powerUps;
        powerUps=new ArrayList<>();

        powerUps.add(powerUp1);
        powerUps.add(powerUp2);
        powerUps.add(powerUp3);
        powerUp1.setFitHeight(156);
        powerUp1.setFitWidth(100);
        powerUp2.setFitHeight(156);
        powerUp2.setFitWidth(100);
        powerUp3.setFitHeight(156);
        powerUp3.setFitWidth(100);
        Image image;

        Color c;
        int i=0;
        //iterates the poweUps of the player for letting themload on the guiMap
        while(i<gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size()) {
            pt = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getType();
            c = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getColor();
            if (pt == PowerUpType.TELEPORTER) {
                if (c== Color.BLUE) {
                    image=new Image("/images/powerUp/teleportBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c==Color.RED) {
                     image=new Image("/images/powerUp/teleportRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/teleportYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else if (pt== PowerUpType.NEWTON) {
                if (c== Color.BLUE) {
                     image=new Image("/images/powerUp/kineticBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c==Color.RED) {
                    image=new Image("/images/powerUp/kineticRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/kineticYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else if (pt==PowerUpType.TAG_BACK_GRENADE) {
                if (c== Color.BLUE) {
                     image=new Image("/images/powerUp/venomBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c==Color.RED) {
                     image=new Image("/images/powerUp/venomRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/venomYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else//TARGETING_SCOPE
            {
                if (c== Color.BLUE) {
                     image=new Image("/images/powerUp/aimBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c== Color.RED) {
                     image=new Image("/images/powerUp/aimRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/aimYellow.png");
                    powerUps.get(i).setImage(image);
                }
            }
            i++;
        }
    }


}
