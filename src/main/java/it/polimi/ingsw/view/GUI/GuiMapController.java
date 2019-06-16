package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.Move;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


import java.awt.*;
import java.util.ArrayList;


public class GuiMapController {

    private static Gui gui;
    private int rows=3,col=4;
    private  VBox map[][]=new VBox[rows][col];
    public static void setGui(Gui g) {
        gui = g;
    }
    private ArrayList <Directions> movementDirections;
    private int validMove=-1;
    private Point currPos;


    @FXML
    BorderPane pane;
    @FXML
    TextArea log;
    @FXML
    GridPane innerMap;

    @FXML
    VBox b00,b01,b02,b03,b10,b11,b12,b13,b20,b21,b22,b23;;
    @FXML
    ImageView powerUp1,powerUp2,powerUp3;

    @FXML
    Button stopMov;


    @FXML
    public void initialize() {

    }

    private Point getPlayerPosFromServer(int id)
    {
        return new Point(gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosX(),gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosY());


    }

    public void mapCreator()
    {


        switch(gui.getView().getCacheModel().getMapType()) {
            case 1: {
                innerMap.setStyle("-fx-background-image: url('/images/Map1in.png')");//use this for sample fot he
                buttonCreator();
                 b03.setOnMouseClicked(new EventHandler<MouseEvent>(){

                     @Override
                     public void handle(MouseEvent mouseEvent) {
                         unClickAble();
                     }
                 });
                b20.setOnMouseClicked(new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        unClickAble();
                    }
                });

                break;
            }
            case 2: {
                buttonCreator();
                innerMap.setStyle("-fx-background-image: url('/images/Map2in.png')");//use this for sample fot he maps everytime
                b20.setOnMouseClicked(new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent mouseEvent) {
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
        b02.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {
                spawn();
            }
        });
        b10.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {
                spawn();
            }
        });
        b23.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {
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
        {map[x][y+1].setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("EAST",x,y)) {
                    movementDirections.add(Directions.EAST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y+1]);
                    eventMover(x,y+1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x<2)
        {map[x+1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("SOUTH",x,y))
                {   movementDirections.add(Directions.SOUTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x+1][y]);
                    eventMover(x+1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(y-1>=0)
        {map[x][y-1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("WEST",x,y)) {
                    movementDirections.add(Directions.WEST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y-1]);
                    eventMover(x,y-1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x-1>=0)
        {map[x-1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("NORTH",x,y)) {
                    movementDirections.add(Directions.NORTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x-1][y]);
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
                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

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
                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

                    }

                });
            }
        }
        int M=m-1;
        //buttons here enable the movements in adjacent cells
        if(y<3)
        {map[x][y+1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("EAST",x,y)) {
                    movementDirections.add(Directions.EAST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y+1]);

                    eventMover(x,y+1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x<2)
        {map[x+1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("SOUTH",x,y))
                {   movementDirections.add(Directions.SOUTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x+1][y]);
                    eventMover(x+1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(y-1>=0)
        {map[x][y-1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("WEST",x,y)) {
                    movementDirections.add(Directions.WEST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y-1]);
                    eventMover(x,y-1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x-1>=0)
        {map[x-1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("NORTH",x,y)) {
                    movementDirections.add(Directions.NORTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                   fromIDtoIMG(gui.getView().getPlayerId(), map[x-1][y]);
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
    private void fromIDtoIMG(int id,VBox b)
    {
        if(b.getChildren().size()==0 || b.getChildren().size()==3)
        {
            Platform.runLater(() ->  {
                b.getChildren().add(new HBox());
                inserter(id, (HBox) b.getChildren().get(0));
            });
            return;
        }
        if(b.getChildren().size()<=3)
        {
            Platform.runLater(() ->  {inserter(id, (HBox) b.getChildren().get(0));});
            return;
        }
        Platform.runLater(() ->  {inserter(id, (HBox) b.getChildren().get(1));});

    }

    private void playerRemover(int id,int x,int y)
    {
        if(map[x][y].getChildren().size()==1)//primo HBOX
        {
            int j=0;

            while(((HBox)map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id))!=0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
            {
                j++;
            }
            ((HBox)map[x][y].getChildren().get(0)).getChildren().remove(j);
        }else{//secondo HBox stessa procedure di prima
            int j=0;

            while(((HBox)map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id))!=0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
            {
                j++;
            }
            ((HBox)map[x][y].getChildren().get(1)).getChildren().remove(j);
        }
    }
    private void inserter(int id,HBox h)
    {
        ImageView img=new ImageView();
        Image image;
        switch (id) {
            case 0:
                image=new Image("/images/player0.png");
                img.setImage(image);
                img.setId("0");
                h.getChildren().add(img);

                break;
            case 1:
                image=new Image("/images/player0.png");
                img.setImage(image);
                h.getChildren().add(img);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
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
            Color c=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0));
            mapPos(colorToCord(c).x,colorToCord(c).y,gui.getView().getPlayerId());
        });
        powerUp2.setOnMouseClicked((e) -> {
            powerUp2.setImage(null);
            Color c=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1));
            mapPos(colorToCord(c).x,colorToCord(c).y,gui.getView().getPlayerId());
        });
        powerUp3.setOnMouseClicked((e) -> {
            powerUp3.setImage(null);
            Color c=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2));
            mapPos(colorToCord(c).x,colorToCord(c).y,gui.getView().getPlayerId());
        });
    }

    private void mapPos(int r,int c,int id)
    {
            //System.out.println("riga: "+r+"colonna :"+c);
            ;
            boolean found=false;
            if( map[r][c].getChildren().size()!=0 && ((HBox)map[r][c].getChildren().get(0)).getChildren()!=null)
            {for(int j=0;j<((HBox)map[r][c].getChildren().get(0)).getChildren().size();j++)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
             {
                 if(((HBox)map[r][c].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id))==0)
                 {
                     found=true;
                 }

             }
             if(found)return;}
             fromIDtoIMG(id, map[r][c]);
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

    private Point colorToCord(Color c)
    {
        Point p=new Point();
           if(c==Color.BLUE)
           {
               p.x=0;
               p.y=2;
               return p;
           }
           if(c==Color.RED)
           {
               p.x=1;
               p.y=0;
               return p;
           }
           if(c==Color.YELLOW)
           {
               p.x=2;
               p.y=3;
               return p;
           }return p;
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
