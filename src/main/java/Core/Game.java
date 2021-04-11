package Core;
import BoardDesign.*;
import Characters.*;
import TileAction.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;

import javafx.scene.*;

import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.scene.text.*;
import javafx.scene.image.*;

import java.io.File;
import java.lang.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


import javafx.util.Duration;
//import org.graalvm.compiler.nodeinfo.StructuralInput;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import javafx.scene.Node.*;
import javafx.scene.control.Button;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game class implements the JavaFX Application class.
 * This class handles game rendering as well as game data
 * including the player score and time elapsed. The game
 * is started and ended from this class. Input listening
 * is also done from this class using EventHandler.
 *
 * @author Danyaal
 * @author Peter
 * @author Brendan
 * @author Stephen
 * @version 1.0
 * @since 1.0
 */
public class Game extends Application{
    private static int score;
    private static int time;

    private static TimerTask gameTicksTask;
    private static Timer gameTicks;
    private static int ticksElapsed = 0; // a tick is 1 second, enemies move per 2
    private static int timeOfInput = 0;
    private static boolean paused = false;

    private int xTileSize = 96;
    private int yTileSize = 96;
    private boolean onFullSecondNextRound = false;

    private static String winStatus = "";

    private static MainCharacter mainCharacter = MainCharacter.getMainCharacter(0, 0);
    private static ArrayList<Enemy> enemyArrayList = new ArrayList<>();

    private GameMenu gameMenu;

    private static String currentStage = "first"; // can be "first", "second", "third", "win", "lose"
    private static Board boardGame;

    private static InputStream spriteStream;
    private static InputStream enemyStream;
    private static InputStream groundStream;
    private static InputStream wallStream;
    private static InputStream checkpointStream;
    private static InputStream punishStream;
    private static InputStream exitStream;
    private static InputStream entryStream;


    private static Image spriteImage = null;
    private static Image enemyImage = null;
    private static Image groundImage = null;
    private static Image wallImage = null;
    private static Image checkpointImage = null;
    private static Image punishImage = null;
    private static Image exitImage = null;
    private static Image entryImage = null;

    private static boolean gameOver = false;



    // no constructor needed since this will contain the main for now

    // launch automatically calls start

    private static int[][] lvl1 = new int[][]{
        {1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 3, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1},
        {1, 2, 0, 0, 0, 2, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1},
        {1, 1, 1, 0, 1, 0, 1, 1, 0, 3, 0, 3, 0, 0, 0, 1},
        {1, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 1, 1, 0, 1, 1, 3, 0, 0, 3, 0, 1, 0, 1},
        {1, 3, 0, 0, 1, 0, 1, 2, 0, 0, 1, 1, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 2, 0, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1}
    };
    private static int[][] lvl2 = new int[][]{
            {1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 0, 0, 0, 2, 1},
            {1, 0, 1, 3, 1, 0, 1, 1, 0, 0, 2, 0, 0, 1, 0, 1},
            {1, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 0, 1, 0, 1, 1, 0, 3, 0, 3, 0, 0, 0, 1},
            {1, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 3, 0, 0, 3, 0, 1, 0, 1},
            {1, 3, 0, 0, 1, 0, 1, 2, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 2, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1}
    };
    private static int[][] lvl3 = new int[][]{
            {1, 5, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 1},
            {1, 0, 1, 3, 1, 1, 3, 0, 0, 3, 1, 1, 3, 1, 0, 1},
            {1, 0, 3, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 2, 2, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 2, 2, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 3, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 3, 0, 1},
            {1, 0, 1, 3, 1, 1, 3, 0, 0, 3, 1, 1, 3, 1, 0, 1},
            {1, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    /**
     * Creates the first level of the game
     */
    private static Board firstStage() {
        enemyArrayList.clear();
        Board temp = createBoard(lvl1);
        Game.generateEnemies();
        return temp;
    }

    /**
     * Creates the second level of the game
     * Ensures that all enemies that were in the first level are cleared
     */
    private static Board secondStage() {
        enemyArrayList.clear(); // delete old enemies
        Board temp = createBoard(lvl2);
        Game.generateEnemies2();
        return temp;
    }

    /**
     * Creates the third and final level of the game
     */
    private static Board thirdStage() {
        enemyArrayList.clear(); // delete old enemies
        Board temp = createBoard(lvl3);
        Game.generateEnemies3();
        return temp;
    }

    public void start(Stage mainGame) {
        mainGame.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        mainGame.setTitle("Maze Game");

        //startGame();
        boardGame = firstStage();
        int squaredBoard = 10;

//        Group root = new Group();
//        Scene theScene = new Scene(root);
//        mainGame.setScene(theScene);

//        Canvas canvas = new Canvas(100 + width*100 , 100 + height*100);
//        root.getChildren().add( canvas);

//        Rectangle[][] rect = new Rectangle[height][width];
//
//        for(int i = 0; i < height; i++){
//            for(int j = 0; j < width; j++){
//                rect[i][j] = new Rectangle();
//            }
//        }

        //Label labelCenter = new Label("this is BorderPane center");
        //Label labelTop = new Label("this is BorderPane top");
        //Label labelBottom = new Label("this is BorderPane bottom");
        //Label labelLeft = new Label("this is BorderPane left");
        //Label labelRight = new Label("this is BorderPane right");

        AnchorPane root = new AnchorPane();
        BorderPane positions = new BorderPane();

        Group rootGroup = new Group(root);
        positions.setCenter(rootGroup);
        positions.setAlignment(rootGroup, Pos.CENTER);

        //positions.setCenter(root);

        //mainGame.setFullScreen(true);

        Scene scene = new Scene(positions);
        scene.setRoot(positions);


        //Lose Gameover Scene
        Pane gameOverRoot = new Pane();
        gameOverRoot.setPrefSize(800,600);
        InputStream inputOverBackground;
        try {
            inputOverBackground = new FileInputStream("assets/YouLost.jpg");

        } catch(FileNotFoundException e) { inputOverBackground = null;}

        Image inputBackgroundOver = new Image(inputOverBackground);

        ImageView imgView2 = new ImageView(inputBackgroundOver);
        imgView2.setFitHeight(600);
        imgView2.setFitWidth(800);

        GameOverMenu gameOverMenu = new GameOverMenu(mainGame, scene, score, time);

        gameOverRoot.getChildren().addAll(imgView2, gameOverMenu);
        BorderPane gameOverBorder = new BorderPane();
        Group gameRoot2 = new Group(gameOverRoot);

        gameOverBorder.setCenter(gameRoot2);
        gameOverBorder.setAlignment(gameRoot2, Pos.CENTER);
        Scene gameIsOver = new Scene(gameOverBorder);
        //Lose Gameover scene

        //Win Gameover Scene
        Pane wgameOverRoot = new Pane();
        wgameOverRoot.setPrefSize(800,600);
        InputStream winputOverBackground;
        try {
            winputOverBackground = new FileInputStream("assets/YouWon.jpg");

        } catch(FileNotFoundException e) { winputOverBackground = null;}

        Image winputBackgroundOver = new Image(winputOverBackground);

        ImageView wimgView2 = new ImageView(winputBackgroundOver);
        wimgView2.setFitHeight(600);
        wimgView2.setFitWidth(800);

        GameOverMenu wgameOverMenu = new GameOverMenu(mainGame, scene, score, time);

        wgameOverRoot.getChildren().addAll(wimgView2, wgameOverMenu);
        BorderPane wgameOverBorder = new BorderPane();
        Group wgameRoot2 = new Group(wgameOverRoot);

        wgameOverBorder.setCenter(wgameRoot2);
        wgameOverBorder.setAlignment(wgameRoot2, Pos.CENTER);
        Scene wgameIsOver = new Scene(wgameOverBorder);
        //Win Gameover scene

        //INstructions scene
        Pane instructionsRoot = new Pane();
        instructionsRoot.setPrefSize(800,600);
        InputStream inputInstructionsBackground;
        try {
            inputInstructionsBackground = new FileInputStream("assets/EscapeCorona2.jpg");

        } catch(FileNotFoundException e) { inputInstructionsBackground = null;}

        Image inputBackgroundInstructions = new Image(inputInstructionsBackground);

        ImageView imgView3 = new ImageView(inputBackgroundInstructions);
        imgView3.setFitHeight(600);
        imgView3.setFitWidth(800);

        InstructionsScreen instructionsMenu = new InstructionsScreen(mainGame, scene);

        instructionsRoot.getChildren().addAll(imgView3, instructionsMenu);
        BorderPane instructionsBorder = new BorderPane();
        Group instructionsGroup = new Group(instructionsRoot);

        instructionsBorder.setCenter(instructionsGroup);
        instructionsBorder.setAlignment(instructionsGroup, Pos.CENTER);
        Scene instructionScene = new Scene(instructionsBorder);


        //MenuStart Scene
        Pane paneRoot = new Pane();
        paneRoot.setPrefSize(800,600);
        InputStream inputBackground;

        try {
            inputBackground = new FileInputStream("assets/EscapeCorona2.jpg");

        } catch(FileNotFoundException e) { inputBackground = null;}

        Image inputBackgroundImage = new Image(inputBackground);

        ImageView imgView = new ImageView(inputBackgroundImage);
        imgView.setFitHeight(600);
        imgView.setFitWidth(800);

        gameMenu = new GameMenu(mainGame, scene, wgameIsOver, instructionScene);

        /*Label label1= new Label("This is the main menu");
        Button button1= new Button("Start Game");
        button1.setOnAction(e -> mainGame.setScene(scene));
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, button1);*/

        paneRoot.getChildren().addAll(imgView, gameMenu);
        BorderPane menuBorder = new BorderPane();
        Group menuRoot = new Group(paneRoot);

        menuBorder.setCenter(menuRoot);
        menuBorder.setAlignment(menuRoot, Pos.CENTER);

        Scene menuStart = new Scene(menuBorder);
        //End of MenuStart

        //Pause Screen
        Pane pauseRoot = new Pane();
        pauseRoot.setPrefSize(800,600);
        InputStream pauseBackground;

        try {
            pauseBackground = new FileInputStream("assets/PauseScreen.jpg");

        } catch(FileNotFoundException e) { pauseBackground = null;}

        Image pauseImage = new Image(pauseBackground);

        ImageView pauseView = new ImageView(pauseImage);
        pauseView.setFitHeight(600);
        pauseView.setFitWidth(800);

        //InstructionsScreen pauseMenu = new InstructionsScreen(mainGame, scene);

        pauseRoot.getChildren().addAll(pauseView);
        BorderPane pauseBorder = new BorderPane();
        Group rootPause = new Group(pauseRoot);

        pauseBorder.setCenter(rootPause);
        pauseBorder.setAlignment(rootPause, Pos.CENTER);

        Scene pauseScene = new Scene(pauseBorder);

        //End of PaueScreen
        Group g2 = new Group();
        //Scene mainmenu = new Scene(g2, 150, 100);

        Group g1 = new Group();

        //Label t1 = new Label("Maingame");
        //Button b1 = new Button("Go to main menu");
        //Label t2 = new Label("This is the main menu");
        //Button b2 = new Button("Go to the maingame");
        //t1.setTranslateY(15);
        //t2.setTranslateY(15);
        //b1.setTranslateY(50);
        //b2.setTranslateY(50);
        //positions.setLeft(b1);
        //positions.setLeft(t1);
        //positions.setAlignment(b1, Pos.CENTER);
        //positions.setAlignment(t1, Pos.CENTER);
        //g1.getChildren().addAll(t1, b1);
        //g2.getChildren().addAll(t2, b2);

        //positions.setLeft(g1);
        //positions.setAlignment(g1, Pos.CENTER);
   // positions.setLeft(b1);
    //positions.setLeft(t1);

        //b1.setOnMouseClicked(e -> { positions.setCenter(g2);});
        //b2.setOnMouseClicked(e -> { positions.setCenter(rootGroup);
        //                            positions.setLeft(g1);});
        /*

        StackPane stack = new StackPane();

        stack.getChildren().add(root);
        stack.setLayoutX((mainGame.getWidth()/2) - stack.getWidth()/2);
        stack.setLayoutY((mainGame.getHeight()/2) - stack.getHeight()/2);

        */
        //positions.setCenter(stack);

        //comment for new branch
//        FileInputStream inputStream = new FileInputStream("assets/Mossy Tileset/Mossy - TileSet.png");
//        Image image = new Image(inputStream);
//        ImageView imageView = new ImageView(image);

//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.setFill( Color.GREEN );
//        gc.setStroke( Color.GREEN );
//        gc.setLineWidth(2);
//        Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
//        gc.setFont( theFont );
//        gc.fillText( "Start Game", 60, 50 );
//        gc.strokeText( "Start Game", 60, 50 );

        //xTileSize = (int)(mainGame.getHeight()/boardGame.getDimY());
        //yTileSize = (int)(mainGame.getHeight()/boardGame.getDimY());

        scene.setRoot(positions);
        mainGame.setScene(menuStart);

        try{
            spriteStream = new FileInputStream("assets/bonnie.png");
            spriteImage = new Image(spriteStream);
            enemyStream = new FileInputStream("assets/enemy.png");
            enemyImage = new Image(enemyStream);
            groundStream = new FileInputStream("assets/grass.png");
            groundImage = new Image(groundStream);
            wallStream = new FileInputStream("assets/wall.png");
            wallImage = new Image(wallStream);
            checkpointStream = new FileInputStream("assets/chkpt.png");
            checkpointImage = new Image(checkpointStream);
            punishStream = new FileInputStream("assets/punish.png");
            punishImage = new Image(punishStream);
            exitStream = new FileInputStream("assets/exit.png");
            exitImage = new Image(exitStream);
            entryStream = new FileInputStream("assets/entry.png");
            entryImage = new Image (entryStream);
        } catch(FileNotFoundException e) {
            spriteStream = null; spriteImage = null;
            enemyStream = null; enemyImage = null;
            groundStream = null; groundImage = null;
            wallStream = null; wallImage = null;
            punishStream = null; punishImage = null;
            exitStream = null; exitImage = null;
            entryStream = null; entryImage = null;
        }

        drawRectangles(root, boardGame, mainCharacter);

        Timeline everySecond = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                //int currentScore = getScore();
                                if(winStatus.equals("You lost. :(") && !(gameOver)) {
                                    GameOverMenu GameOverMenu2 = new GameOverMenu(mainGame, scene, score, time);
                                    Pane gameOverRoot2 = new Pane();
                                    gameOverRoot2.setPrefSize(800,600);
                                    gameOverRoot2.getChildren().addAll(imgView2, GameOverMenu2);
                                    BorderPane gameOverBorder2 = new BorderPane();
                                    Group gameOver2 = new Group(gameOverRoot2);

                                    gameOverBorder2.setCenter(gameOver2);
                                    gameOverBorder2.setAlignment(gameOver2, Pos.CENTER);

                                    Scene gameIsOver2 = new Scene(gameOverBorder2);
                                    mainGame.setScene(gameIsOver2);
                                    gameOver = true;
                                }
                                else if(winStatus.equals("You won!") && !(gameOver)) {
                                    GameOverMenu WinGameOverMenu = new GameOverMenu(mainGame, scene, score, time);
                                    Pane wingameOverRoot = new Pane();
                                    wingameOverRoot.setPrefSize(800, 600);
                                    wingameOverRoot.getChildren().addAll(wimgView2, WinGameOverMenu);
                                    BorderPane wingameOverBorder = new BorderPane();
                                    Group wingameOver = new Group(wingameOverRoot);

                                    wingameOverBorder.setCenter(wingameOver);
                                    wingameOverBorder.setAlignment(wingameOver, Pos.CENTER);

                                    Scene winGameIsOver = new Scene(wingameOverBorder);
                                    mainGame.setScene(winGameIsOver);
                                    gameOver = true;
                                }
                                Integer getScoreInt = new Integer(getScore());
                                Integer getTimeInt = new Integer(time);
                                Integer getCheckpointsRemainingInt = new Integer(Checkpoint.getCheckpointsLeft());
                                HBox statistics = new HBox();
                                VBox firstChild = new VBox();
                                VBox secondChild = new VBox();
                                VBox thirdChild = new VBox();
                                VBox fourthChild = new VBox();
                                int numberOfChildren = 4;
                                Text scoreDisplay = new Text("Current Score: " + getScoreInt.toString());
                                Text timeDisplay = new Text("Time Elapsed: " + getTimeInt.toString());
                                Text checkpointDisplay = new Text("Checkpoints Remaining: " + getCheckpointsRemainingInt.toString());
                                Text winDisplay = new Text("Win Status: " + winStatus);
                                firstChild.getChildren().add(scoreDisplay);
                                secondChild.getChildren().add(timeDisplay);
                                thirdChild.getChildren().add(checkpointDisplay);
                                fourthChild.getChildren().add(winDisplay);
                                statistics.setPrefWidth(mainGame.getWidth());
                                firstChild.setPrefWidth(mainGame.getWidth()/numberOfChildren);
                                secondChild.setPrefWidth(mainGame.getWidth()/numberOfChildren);
                                thirdChild.setPrefWidth(mainGame.getWidth()/numberOfChildren);
                                fourthChild.setPrefWidth(mainGame.getWidth()/numberOfChildren);
                                // maybe use escape to pause game or something?
                                if (!paused) {
                                    if (onFullSecondNextRound) {
                                        time = time + 1;
                                        onFullSecondNextRound = false;
                                    } else {
                                        onFullSecondNextRound = true;
                                    }
                                }

                                statistics.getChildren().add(firstChild);
                                statistics.getChildren().add(secondChild);
                                statistics.getChildren().add(thirdChild);
                                statistics.getChildren().add(fourthChild);

                                xTileSize = (int)(mainGame.getHeight()/boardGame.getDimY())-6-(int)Math.ceil(statistics.getHeight()/boardGame.getDimY());
                                yTileSize = (int)(mainGame.getHeight()/boardGame.getDimY())-6-(int)Math.ceil(statistics.getHeight()/boardGame.getDimY());
                                drawRectangles(root, boardGame, mainCharacter);

                                positions.setTop(statistics);
                            }
                        }));
        everySecond.setCycleCount(Timeline.INDEFINITE);
        everySecond.play();

        //game timer
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.ESCAPE) {
                    if (!paused) {
                        everySecond.pause();
                        //mainGame.setScene(pauseScene);
                        //OLD CODE THAT WORKS
                        Label t2 = new Label("This is the main menu. Press ESCAPE to resume");
                        Button b2 = new Button("Go to the maingame");
                        t2.setTranslateY(15);

                        //OLD CODE THAT WORKS ENDING

                        //b2.setTranslateY(50);
                        //b2.setOnMouseClicked(f -> { positions.setCenter(rootGroup);});

                        //Old code that works part2
                        g2.getChildren().clear();
                        g2.getChildren().addAll(t2, pauseView);
                        positions.setCenter(g2);
                        //Old Code that works partending
                    } else {
                        everySecond.play();
                        //old code that works
                       positions.setCenter(rootGroup);
                        //old code that works ending
                       /* mainGame.setScene(scene);*/
                    }
                    if (winStatus == "") {
                        paused = !paused;
                    }

                } else if (!paused && ((ticksElapsed-timeOfInput) >= 1)) {
                    timeOfInput = ticksElapsed;
                    mainCharacter.keyPressed(e);
                    drawRectangles(root, boardGame, mainCharacter);
                }
            }
        });

//        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent e) {
//                mainCharacter.keyReleased(e);
//            }
//        });

        mainGame.show();
    }



    private class GameMenu extends Parent{
        //scene2 is passed for the gameendscreen DEBUG
        public GameMenu(Stage mainGame, Scene scene, Scene scene2, Scene scene3) {
            VBox menuOrig = new VBox(40);
            VBox menu2 = new VBox(10);

            menuOrig.setTranslateX(250);
            menuOrig.setTranslateY(250);

            menu2.setTranslateX(100);
            menu2.setTranslateY(200);

            final int offset = 400;

            MenuButton resumeBtn = new MenuButton("START GAME");

            resumeBtn.setOnMouseClicked(event -> {
                mainGame.setScene(scene);
                startGame();
            });
            MenuButton instructionsBtn = new MenuButton("INSTRUCTIONS");
            instructionsBtn.setOnMouseClicked(event -> {
                mainGame.setScene(scene3);
            });

            MenuButton exitBtn = new MenuButton("EXIT");
            exitBtn.setOnMouseClicked(event ->{
                System.exit(0);
            });

            MenuButton debugOverBtn = new MenuButton("DEBUG WONGAME");
            debugOverBtn.setOnMouseClicked(event ->{
                mainGame.setScene(scene2);
            });

            menuOrig.getChildren().addAll(resumeBtn, instructionsBtn, exitBtn, debugOverBtn);

            Rectangle background = new Rectangle(800,600);
            background.setFill(Color.GREY);
            background.setOpacity(0.4);

            getChildren().addAll(background, menuOrig);


        }
    }
    private class InstructionsScreen extends Parent{
        private Text text;
        //scene2 is passed for the gameendscreen DEBUG
        public InstructionsScreen(Stage mainGame, Scene scene) {
            VBox menuOrig = new VBox(40);
            VBox menu2 = new VBox(10);

            menuOrig.setTranslateX(250);
            menuOrig.setTranslateY(100);

            menu2.setTranslateX(100);
            menu2.setTranslateY(200);

            final int offset = 400;


            MenuButton resumeBtn = new MenuButton("START GAME");
            resumeBtn.setOnMouseClicked(event -> {
                mainGame.setScene(scene);
                startGame();
            });

            MenuButton exitBtn = new MenuButton("EXIT");
            exitBtn.setOnMouseClicked(event ->{
                System.exit(0);
            });

            //Label label1= new Label("This is the instructions");
            //label1.setPrefSize(1000,250);
            text = new Text("Instructions: Play as Bonnie Henry in her mission to Escape the Coronavirus: Your objective is to pick up all blue checkpoints, and then proceed to the red exit to advance to the next level. Move the character with arrow keys, and avoid squares with needles to avoid score punishments. Pick up PPE to improve ur score! If score is negative, you die. Make sure to dodge CoronaEnemies to keep from dying as well!");
            text.setFont(text.getFont().font(20));
            text.setFill(Color.BLACK);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setWrappingWidth(550);
            text.setTranslateX(-125);

            /*Rectangle menuR = new Rectangle(250, 50);
            menuR.setOpacity(0.6);
            menuR.setFill(Color.BLACK);
            getChildren().addAll(menuR, text);*/


            menuOrig.getChildren().addAll(resumeBtn, exitBtn, text);

            Rectangle background = new Rectangle(800,600);
            background.setFill(Color.GREY);
            background.setOpacity(0.4);

            getChildren().addAll(background, menuOrig);


        }
    }

    private class GameOverMenu extends Parent{
        public GameOverMenu(Stage mainGame, Scene scene, int endScore, int endTime) {
            VBox menuOrig = new VBox(40);
            VBox menu2 = new VBox(10);

            menuOrig.setTranslateX(250);
            menuOrig.setTranslateY(250);

            menu2.setTranslateX(100);
            menu2.setTranslateY(200);

            final int offset = 400;

            MenuButton resumeBtn = new MenuButton("RESTART");
            resumeBtn.setOnMouseClicked(event -> {
                restartGame();
                mainGame.setScene(scene);
            });
            //int endScore = getScore();
            //System.out.println("score:");
            //System.out.println(endScore);

            MenuButton instructionsBtn = new MenuButton("SCORE: " + endScore);
            MenuButton timeBtn = new MenuButton("TIME: " + endTime + "s");

            MenuButton exitBtn = new MenuButton("EXIT");
            exitBtn.setOnMouseClicked(event ->{
                System.exit(0);
            });

            menuOrig.getChildren().addAll(resumeBtn, instructionsBtn, timeBtn, exitBtn);

            Rectangle background = new Rectangle(800,600);
            background.setFill(Color.GREY);
            background.setOpacity(0.4);

            getChildren().addAll(background, menuOrig);
        }
    }

    private static class Music {
        public void playMusic(String musicLocation) {
            try {
                File musicPath = new File(musicLocation);

                if (musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    Clip clip = AudioSystem.getClip();
                    clip.start();
                } else {
                    System.out.println("Cant find file");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //MenuButton template for buttons
    private static class MenuButton extends StackPane{
        private Text text;

        String coronapath = "assets/coronatime.mp3";


        //Music musicObject = new Music();
       // musicObject.playMusic(coronapath);

        /*Media coronamedia = new Media(new File(coronapath).toURI().toString());
        MediaPlayer coronaPlayer = new MediaPlayer(coronamedia);*/
        public MenuButton(String name){
            text = new Text(name);
            text.setFont(text.getFont().font(20));
            text.setFill(Color.WHITE);

            Rectangle menuR = new Rectangle(250, 50);
            menuR.setOpacity(0.6);
            menuR.setFill(Color.BLACK);
            menuR.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(menuR, text);

            setOnMouseEntered(event -> {
                menuR.setTranslateX(30);
                text.setTranslateX(30);
                menuR.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
                //clickPlayer.play();
            });
            setOnMouseExited(event -> {
                menuR.setTranslateX(0);
                text.setTranslateX(0);
                menuR.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
                //clickPlayer.play();
            });

            DropShadow drop = new DropShadow(50, Color.WHITE);
            drop.setInput(new Glow());


            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> {
                setEffect(null);
                /*coronaPlayer.play();*/
            });


        }
    }

    /**
     * Generates enemies on the in a static manner.
     */
    public static void generateEnemies() { //GENERATING ENEMIES
        Enemy e1 = new Enemy(8, 8);
        Enemy e2 = new Enemy(3, 8);
        enemyArrayList.add(e1);
        enemyArrayList.add(e2);
    }

    public static void generateEnemies2() {
        Enemy e1 = new Enemy(8, 8);
        Enemy e2 = new Enemy(3, 8);
        Enemy e3 = new Enemy(10, 3);
        enemyArrayList.add(e1);
        enemyArrayList.add(e2);
        enemyArrayList.add(e3);
    }

    public static void generateEnemies3() {
        //Enemy e1 = new Enemy(4, 3);
        Enemy e2 = new Enemy(13, 3);
        Enemy e3 = new Enemy(4, 6);
        Enemy e4 = new Enemy(13, 6);
        //enemyArrayList.add(e1);
        enemyArrayList.add(e2);
        enemyArrayList.add(e3);
        enemyArrayList.add(e4);
    }

    /**
     * Calls method to update all Enemy positions.
     */
    public void moveEnemies() {
        for(Enemy e : enemyArrayList) {
            e.move(e.checkBestMovement(enemyArrayList));
            e.printPos();
        }
    }

    /**
     * Called every tick. Handles game updates including
     * Bonus reward generation.
     */
    public static void updateGame() {
        Board.generateBonus();
    }

    /**
     * Draws the mainCharacter.
     *
     * @param root a JavaFX Pane
     * @param boardGame Board used in current game
     * @param mainCharacter the player controlled MainCharacter
     */
    void drawMainCharacter(AnchorPane root, Board boardGame, MainCharacter mainCharacter) {

        Rectangle rect = null;
        InputStream inputStream;
        Image image = spriteImage;

        int x = mainCharacter.getX();
        int y = mainCharacter.getY();
        int width = xTileSize;
        int height = yTileSize;
        rect = new Rectangle(width*x, height*y, width, height);
        if(image != null)
            rect.setFill(new ImagePattern(image));
        else
            rect.setFill(Color.BLACK);
        root.getChildren().add(rect);

    }

    void drawReward(AnchorPane root, Tile tile, int x, int y) {
        //Rectangle rect = null;
        Image image = null;
        int height = yTileSize;
        int width = xTileSize;
        Rectangle rect = new Rectangle(width*x, height*y, width, height);
        rect.toFront();
        if (tile.typeOfReward.equals("Checkpoint")) {
            image = checkpointImage;
            rect.setFill(new ImagePattern(image));

        }
        else if (tile.typeOfReward.equals("Punishment")) {
            if(punishImage != null) {
            image = punishImage;
            rect.setFill(new ImagePattern(image));
            }
            else
                rect.setFill(Color.PINK);
        }
        else if (tile.typeOfReward.equals("Bonus")) {
//            image = bonusImage;
            rect.setFill(Color.YELLOW);
        }
//        rect.setFill(new ImagePattern(image));
        root.getChildren().add(rect);

    }

    void drawEntryExit(AnchorPane root, Image image, int x, int y) {
        int height = yTileSize;
        int width = xTileSize;
        Rectangle rect = new Rectangle(width*x, height*y, width, height);
        rect.toFront();
        rect.toFront();
        rect.setFill(new ImagePattern(image));
        root.getChildren().add(rect);
    }

    /**
     * Draws the enemies.
     *
     * @param root a JavaFX Pane
     * @param boardGame Board used in current game
     */
    void drawEnemies(AnchorPane root, Board boardGame) {
        Rectangle rect = null;
        Image image = enemyImage;

        int width = xTileSize;
        int height = yTileSize;
        for (int i = 0; i < enemyArrayList.size(); i++) {
            int x = enemyArrayList.get(i).getX();
            int y = enemyArrayList.get(i).getY();
            rect = new Rectangle(width*x, height*y, width, height);
            if(image != null)
                rect.setFill(new ImagePattern(image));
            else
                rect.setFill(Color.BLACK);
            root.getChildren().add(rect);
        }
    }

    /**
     * Draws the board Tiles.
     *
     * @param root a JavaFX Pane
     * @param boardGame Board used in current game
     * @param mainCharacter the player controlled MainCharacter
     */
    void drawRectangles(AnchorPane root, Board boardGame, MainCharacter mainCharacter) {
        root.getChildren().clear();
        int width = boardGame.getDimX();
        int height = boardGame.getDimY();
        int horizontal = xTileSize, vertical = yTileSize;
        Rectangle rect = null;
        Rectangle rectSupp = null;
        for(int i = 0; i < height; ++i)
        {//Iterate through columns
            for(int j = 0; j < width; ++j)
            {//Iterate through rows
//              Color choice = chooseColor(rectColors);
                //Method that chooses a color
                //Create a new rectangle(PosY,PosX,width,height)
                rect = new Rectangle(horizontal*j, vertical*i, horizontal, vertical);
                //temporary asset loading for textures; could eventually be done from one file and be more elegant

                Image imageTile = groundImage;
                Image imageWall = wallImage;

                Tile currentTile = boardGame.getTile(i,j);
                String currentTileString = currentTile.getClass().getSimpleName();
                int currentTileInt = 0;
                if (currentTileString.equals("Tile")) {
                    currentTileInt = 0;
                } else if (currentTileString.equals("Wall")) {
                    currentTileInt = 1;
                }
                else if (currentTileString.equals("Entrance")) {
                    currentTileInt = 2;
                }
                else if (currentTileString.equals("Exit")) {
                    currentTileInt = 3;
                }
//                rect.setStrokeWidth(1);
                //rect.setStroke(Color.BLACK);
                rect.setFill(Color.WHITE);
                rect.toBack();

                switch(currentTileInt)
                {
                    case 0: // empty tile
                        rect.setFill(new ImagePattern(imageTile));
                        break;
                    case 1: // wall
                        rect.setFill(new ImagePattern(imageWall));
                        break;
                    case 2: // entrance
                        rect.setFill(new ImagePattern(imageTile));
                        break;
                    case 3: // exit
                        rect.setFill(new ImagePattern(imageTile));
                        break;
                }

                root.getChildren().add(rect);
                //Add Rectangle to board

                boolean tileHasReward = currentTile.getHasReward();
                if (tileHasReward) {
                    drawReward(root, currentTile, j, i);
                }
                switch(currentTileInt) {
                    case 2:
                        drawEntryExit(root, entryImage, j, i);
                        break;
                    case 3:
                        drawEntryExit(root, exitImage, j, i);
                        break;
                }
            }
        }

        drawMainCharacter(root, boardGame, mainCharacter);
        drawEnemies(root, boardGame);
    }

    /**
     * Starts the gameTicks timer.
     */
    private void startTimer() {
        gameTicksTask = new TimerTask() {
            int prevTicksElapsed = 0;
            @Override
            public void run() {
                if(!paused) {
                    ticksElapsed += 1;
                    if((ticksElapsed - prevTicksElapsed) >= 2) {
                        prevTicksElapsed = ticksElapsed;
                        moveEnemies();
                    }
                    updateGame();
                }
            }
        };
        gameTicks = new Timer();

        gameTicks.scheduleAtFixedRate(gameTicksTask, 20, 500);
    }

    /**
     * Initializes the starting variables score and time.
     * Also start the gameTicks timer.
     */
    public void startGame(){
        score = 0;
        time = 0;
        startTimer();
    }

    /**
     * Ends the current game.
     *
     * @param win true if player wins, else false
     */
    public static void endGame(boolean win){
        /*if (winStatus == null) {    // prevents winStatus from changing
            if(win) {
                winStatus = "You win.";
            }
            else {
                winStatus = "You lose.";
            }
            //gameTicks.cancel();
            //paused = true;
        }*/
        if (currentStage.equals("win") || currentStage.equals("lose")) {
            // do nothing
            gameTicks.cancel();
            paused = true;
        }
        else {
            if (win) {
                if (currentStage.equals("first")) {
                    currentStage = "second";
                    boardGame = secondStage();
                } else if (currentStage.equals("second")) {
                    currentStage = "third";
                    boardGame = thirdStage();
                } else if (currentStage.equals("third")) {
                    currentStage = "win";
                    gameTicks.cancel();
                    paused = true;
                    winStatus = "You won!";
                } else if (currentStage.equals("win")) {
                    // do nothing
                } else {
                    System.out.println("Not a valid current stage");
                }
            }
            else {
                currentStage = "lose";
                winStatus = "You lost. :(";
                gameTicks.cancel();
                paused = true;
            }
        }
    }

    /**
     * Resets values and restarts the game to first stage.
     */
    private void restartGame() {
        score = 0;
        time = 0;
        ticksElapsed = 0;
        timeOfInput = 0;
        winStatus = "";
        currentStage = "first";
        paused = false;
        gameOver = false;

        Checkpoint.setCheckpointsLeft(0);
        
        boardGame = firstStage();

        startTimer();
    }

    /**
     * Getter for score.
     *
     * @return the current game score
     */
    public int getScore(){
        return score;
    }

    /**
     * Static getter for score.
     * @return the current game score
     */
    public static int getScoreStatic() {
        return score;
    }

    /**
     * Getter for time.
     *
     * @return the current game time
     */
    public int getTime(){
        return time;
    }

    /**
     * Adds to the game score by specified amount.
     *
     * @param amount the value to add to the total score
     */
    public static void updateScore(int amount){
        score += amount;
        //System.out.println("Score:" + score);
        if(score < 0) {
            endGame(false);
        }
    }

    public static void setCurrentStage(String stage) {
        currentStage = stage;
    }

    public static String getCurrentStage() {
        return currentStage;
    }

    public static String getWinStatus() {
        return winStatus;
    }

    private static Board createBoard(int[][] lvl) {

        Board boardGame = new Board(lvl);
        for (int i = 0; i < boardGame.getDimY(); i++) {
            for (int j = 0; j < boardGame.getDimX(); j++) {
                System.out.print(Board.getBoard()[i][j].getClass().getSimpleName() + Board.getBoard()[i][j].typeOfReward + " ");
            }
            System.out.println("");
        }
        return boardGame;
    }

    public static void main(String[] args) {

        launch(args);
//
//        Game game = new Game();
//        int testscore = game.getScore();
//        System.out.println(testscore);
//        game.changeScore(23);
//        int testscore2 = game.getScore();
//        System.out.println(testscore2);
//        MainCharacter mainCharacter = MainCharacter.getMainCharacter(0, 0);
    }

}
