package utils;

import org.aeonbits.owner.ConfigFactory;

public class PropertyHelper {
    private static AppConfig config;

    public static AppConfig getConf() {
        if (config == null) {
            config = ConfigFactory.create(AppConfig.class);
        }
        return config;
    }
}