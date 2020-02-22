package nl.tudelft.cse.sem.group34.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.tudelft.cse.sem.group34.PacmanGdxGame;

/**
 * Launcher of the desktop application featuring the game 'Pacman'.
 */
public class DesktopLauncher {
    /**
     * Method which is executed upon launching the application.
     * The method launches the game 'Pacman'.
     *
     * @param arg This parameter is not used in the method.
     */

    public static void main(String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PacMan";
        config.width = PacmanGdxGame.WIDTH;
        config.height = PacmanGdxGame.HEIGHT;

        new LwjglApplication(new PacmanGdxGame(), config);

        //LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        //Hardcoded map size
        //config.height = (int) (Constants.windowHeight * Constants.Game.tileSizeY);
        //config.width = (int) (Constants.windowWidth * Constants.Game.tileSizeX);

        //new LwjglApplication(new PacmanGdxGame(), config);

    }

}
