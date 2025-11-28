package art.infra.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class ConfigLoader {
    public static AppConfig load() {
        Yaml yaml = new Yaml();
        InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("config.yml");
        AppConfig appConfig = yaml.loadAs(inputStream, AppConfig.class);
        return appConfig;
    }
}
