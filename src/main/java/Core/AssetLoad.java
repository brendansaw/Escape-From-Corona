package Core;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AssetLoad {
    private InputStream spriteStream;
    private InputStream enemyStream;
    private InputStream groundStream;
    private InputStream wallStream;
    private InputStream checkpointStream;
    private InputStream punishStream;
    private InputStream exitStream;
    private InputStream entryStream;


    private Image spriteImage = null;
    private Image enemyImage = null;
    private Image groundImage = null;
    private Image wallImage = null;
    private Image checkpointImage = null;
    private Image punishImage = null;
    private Image exitImage = null;
    private Image entryImage = null;

    public AssetLoad() {
        reloadAssets();
    }

    public void reloadAssets() {
        /*loadSpriteImage("src/main/resources/assets/bonnie.png");
        loadEnemyImage("src/main/resources/assets/enemy.png");
        loadGroundImage("src/main/resources/assets/grass.png");
        loadWallImage("src/main/resources/assets/wall.png");
        loadCheckpointImage("src/main/resources/assets/chkpt.png");
        loadPunishmentImage("src/main/resources/assets/punish.png");
        loadExitImage("src/main/resources/assets/exit.png");
        loadEntranceImage("src/main/resources/assets/entry.png");*/
        loadSpriteImage("/assets/bonnie.png");
        loadEnemyImage("/assets/enemy.png");
        loadGroundImage("/assets/grass.png");
        loadWallImage("/assets/wall.png");
        loadCheckpointImage("/assets/chkpt.png");
        loadPunishmentImage("/assets/punish.png");
        loadExitImage("/assets/exit.png");
        loadEntranceImage("/assets/entry.png");
    }

    public Image loadSpriteImage(String path) {
        try {
            spriteStream = getClass().getResourceAsStream(path);
            spriteImage = new Image(spriteStream);
        }
        catch (NullPointerException e) {
            spriteStream = null;
            spriteImage = null;
            return spriteImage;
        }
        return spriteImage;
    }

    public Image loadEnemyImage(String path) {
        try {
            enemyStream = getClass().getResourceAsStream(path);
            enemyImage = new Image(enemyStream);
        }
        catch (NullPointerException e) {
            enemyStream = null;
            enemyImage = null;
            return enemyImage;
        }
        return enemyImage;
    }

    public Image loadGroundImage(String path) {
        try {
            groundStream = getClass().getResourceAsStream(path);
            groundImage = new Image(groundStream);
        }
        catch (NullPointerException e) {
            groundStream = null;
            groundImage = null;
            return groundImage;
        }
        return groundImage;
    }

    public Image loadWallImage(String path) {
        try {
            wallStream = getClass().getResourceAsStream(path);
            wallImage = new Image(wallStream);
        }
        catch (NullPointerException e) {
            wallStream = null;
            wallImage = null;
            return wallImage;
        }
        return wallImage;
    }

    public Image loadCheckpointImage(String path) {
        try {
            checkpointStream = getClass().getResourceAsStream(path);
            checkpointImage = new Image(checkpointStream);
        }
        catch (NullPointerException e) {
            checkpointStream = null;
            checkpointImage = null;
            return checkpointImage;
        }
        return checkpointImage;
    }

    public Image loadPunishmentImage(String path) {
        try {
            punishStream = getClass().getResourceAsStream(path);
            punishImage = new Image(punishStream);
        }
        catch (NullPointerException e) {
            punishStream = null;
            punishImage = null;
            return punishImage;
        }
        return punishImage;
    }

    public Image loadExitImage(String path) {
        try {
            exitStream = getClass().getResourceAsStream(path);
            exitImage = new Image(exitStream);
        }
        catch (NullPointerException e) {
            exitStream = null;
            exitImage = null;
            return exitImage;
        }
        return exitImage;
    }

    public Image loadEntranceImage(String path) {
        try {
            entryStream = this.getClass().getResourceAsStream(path);
            entryImage = new Image(entryStream);
        }
        catch (NullPointerException e) {
            entryStream = null;
            entryImage = null;
            return entryImage;
        }
        return entryImage;
    }

    public Image getSpriteImage() {
        return spriteImage;
    }

    public Image getEnemyImage() {
        return enemyImage;
    }

    public Image getGroundImage() {
        return groundImage;
    }

    public Image getWallImage() {
        return wallImage;
    }

    public Image getCheckpointImage() {
        return checkpointImage;
    }

    public Image getPunishImage() {
        return punishImage;
    }

    public Image getEntryImage() {
        return entryImage;
    }

    public Image getExitImage() {
        return exitImage;
    }
}
