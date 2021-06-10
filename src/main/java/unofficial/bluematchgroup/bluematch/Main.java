package unofficial.bluematchgroup.bluematch;

import unofficial.bluematchgroup.bluematch.WindowManager.View;
import unofficial.bluematchgroup.bluematch.config.Config;
import unofficial.bluematchgroup.bluematch.controllers.Controller;
import unofficial.bluematchgroup.bluematch.model.DataSource;
import unofficial.bluematchgroup.bluematch.plugin.PluginManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private static final PluginManager PLUGIN_LOADER = new PluginManager();

    @Override
    public void start(Stage primaryStage) throws Exception {
        PLUGIN_LOADER.loadPlugins();
        View mainView = WindowManager.getInstance().initWindow(primaryStage);
        // controller.updateMainView();
        Config.getInstance().windowWidth = 1024;
        Controller controller = mainView.getController();
        controller.updatePluginList(PLUGIN_LOADER);
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!DataSource.getInstance().open()) {
            TimeUnit.SECONDS.sleep(8);
            System.out.println("Could not connect to database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().close();
    }

    public static void main(String[] args) {

        System.out.println("start program");
        launch(args);
    }
}
