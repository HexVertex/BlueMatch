package unofficial.bluematchgroup.bluematch;

import java.io.File;

public class FileUtils {
    
    private String appHomeDirPath;

    private File homeDir;

    private static FileUtils instance;

    public FileUtils() {
        resolveAppPaths();
        createDefaultFolders();
    }

    private void resolveAppPaths() {
        String workingDir;
        String OS = System.getProperty("os.name").toUpperCase();

        if(OS.contains("WIN")) {
            workingDir = System.getenv("AppData");
        }
        else {
            workingDir = System.getProperty("user.home");
            workingDir += "/Library/Application Support";
        }

        workingDir += "/BlueMatch";

        appHomeDirPath = workingDir;
    }

    private void createDefaultFolders() {
        homeDir = new File(appHomeDirPath);
        createFolders(homeDir);
    }

    public boolean createFolders(File f) {
        if(!f.exists()) {
            try {
                return f.mkdirs();
            } catch(SecurityException e) {
                System.out.println("Failed to create application directory.");
                e.printStackTrace();
            }
        }
        return false;
    }

    public File getHomeDir() {
        return this.homeDir;
    }

    public static FileUtils getInstance() {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }


}
