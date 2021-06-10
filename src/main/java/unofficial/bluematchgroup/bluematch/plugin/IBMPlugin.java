package unofficial.bluematchgroup.bluematch.plugin;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface IBMPlugin {

    /**
     * Gets the displayname for the plugin. This name will be displayed in the
     * plugin list. This name does not need to be unique.
     * 
     * @return DisplayName of the plugin
     */
    public abstract String getDisplayName();

    /**
     * Gets the unique identifier of the plugin. This needs to be unique and
     * preferably unchanging between versions of the plugin.
     * 
     * @return A unique identifier string of the plugin suggestion:
     *         <b>author.exampleplugin</b>
     */
    public abstract String getId();

    /**
     * Gets the version string of the plugin.
     * 
     * @return Plugin version string
     */
    public abstract String getVersionString();

    /**
     * Pre-load function. Placeholder function, avoid using for now.
     */
    public abstract void preLoad();

    /**
     * Main loading function.
     */
    public abstract void onLoad();

    /**
     * Post-load function for potential future inter-plugin interaction.
     */
    public abstract void postLoad();

    
    public abstract Scene initScene(Stage stage);
}
