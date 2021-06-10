package unofficial.bluematchgroup.bluematch.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unofficial.bluematchgroup.bluematch.FileUtils;
import unofficial.bluematchgroup.bluematch.WindowManager;

public class PluginManager {

    private static ArrayList<IBMPlugin> plugins = new ArrayList<IBMPlugin>();
    private static final String PLUGIN_DIRECTORY = "plugins";

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    private String getMD5(Path p) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(p));
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private void copyDefaultPlugins(File pluginDir) {
        try {
            Path targetPath = pluginDir.toPath();
            URI uri = this.getClass().getClassLoader().getResource(PLUGIN_DIRECTORY).toURI();
            Path p;
            FileSystem fs = null;
            if (uri.getScheme().equals("jar")) {
                fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                p = fs.getPath(PLUGIN_DIRECTORY);
            } else {
                p = Paths.get(uri);
            }
            Stream<Path> walk = Files.walk(p, 1);
            walk.forEach(s -> {
                if (s.toString().endsWith(".jar")) {
                    try {
                        // System.out.println(Paths.get(targetPath.toString(),
                        // s.getFileName().toString()));

                        Path tp = Paths.get(targetPath.toString(), s.getFileName().toString());
                        if (Files.exists(tp)) {
                            if (getMD5(s).equals(getMD5(tp))) {
                                System.out
                                        .println(s.getFileName().toString() + " exists and matches MD5: " + getMD5(s));
                                return;
                            }
                        }
                        System.out.println("Copying: " + s.getFileName().toString());
                        Files.copy(s, tp, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            walk.close();
            if (fs != null) {
                fs.close();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    public void loadPlugins() {
        try {
            File pluginDir = new File(FileUtils.getInstance().getHomeDir(), PLUGIN_DIRECTORY);

            System.out.println("Attempting to load plugins from " + pluginDir.getAbsolutePath());

            if (!pluginDir.exists()) {
                System.out.println("Plugin folder missing! Attempting to create...");
                if (!FileUtils.getInstance().createFolders(pluginDir))
                    return;
            }

            copyDefaultPlugins(pluginDir);

            File[] files = pluginDir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getPath().toLowerCase().endsWith(".jar");
                }
            });

            URL[] urls = new URL[files.length];

            for (int i = 0; i < files.length; i++) {
                urls[i] = files[i].toURI().toURL();
            }
            URLClassLoader ucl = new URLClassLoader(urls);

            ServiceLoader<IBMPlugin> loader = ServiceLoader.load(IBMPlugin.class, ucl);
            for (IBMPlugin plugin : loader) {
                System.out.println(plugin.getId() + " detected!");
                plugin.preLoad();
                plugins.add(plugin);
                plugin.onLoad();
                System.out.println(plugin.getId() + " loaded!");
            }
            for (IBMPlugin plugin : plugins) {
                plugin.postLoad();
            }
        } catch (IOException e) {
            System.out.println("Failed to load plugins...");
            e.printStackTrace();
        }
    }

    public ArrayList<IBMPlugin> getPlugins() {
        return plugins;
    }

    public int openPlugin(IBMPlugin plugin) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.NONE);
        stage.initOwner(WindowManager.getInstance().getWindow());
        stage.setTitle(plugin.getDisplayName() + " - " + plugin.getVersionString());
        Scene pluginScene = plugin.initScene(stage);
        if(pluginScene != null) {
            stage.setScene(pluginScene);
            stage.sizeToScene();
            stage.show();
        }
        else {
            System.out.println("Failed to open plugin: " + plugin.getDisplayName() + " - " + plugin.getVersionString());
        }
        return 0;
    }
}
