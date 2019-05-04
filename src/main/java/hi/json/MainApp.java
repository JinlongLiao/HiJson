/*
 * MainApp.java
 */

package hi.json;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the application.
 */
public class MainApp extends SingleFrameApplication {

    private static String[] args;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new MainView(this, args));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of MainApp
     */
    public static MainApp getApplication() {
        return Application.getInstance(MainApp.class);
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MainApp.class);

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

        for (String arg : args) {
            logger.info("启动参数" + arg);
        }
        MainApp.args = args;
        launch(MainApp.class, args);
    }
}
