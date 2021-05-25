package unofficial.bluematchgroup.bluematch.config;

public class Config {

    private static Config instance;

    public int windowWidth;

    public Config() {
        
    }

    public static Config getInstance() {
        if(instance == null) {
            synchronized(Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }
}
