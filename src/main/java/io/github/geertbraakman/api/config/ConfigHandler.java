package io.github.geertbraakman.api.config;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.exceptions.ConfigLoadException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler extends Handler {

    private static final String EXTENSION = ".yml";

    private List<APIConfig> APIConfigList;

    public ConfigHandler(APIPlugin plugin) {
        super(plugin);
        APIConfigList = new ArrayList<>();
    }

    @Override
    public boolean reload() {
        for(APIConfig APIConfig : APIConfigList){
            if(!APIConfig.reload()){
                return false;
            }
        }
        return true;
    }

    static String addFileExtension(String name) {
        if (!name.endsWith(EXTENSION)){
            name += EXTENSION;
        }
        return name;
    }

    YamlConfiguration loadConfig(String fileName) throws ConfigLoadException {
        File file = new File(getAPIPlugin().getDataFolder(), fileName);
        if (!file.exists()) {
//            if (!file.getParentFile().mkdirs()){
//                throw new ConfigLoadException(fileName, "Could not create the file");
//            }
            file.getParentFile().mkdirs();
            getAPIPlugin().saveResource(fileName, false);
        }

        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new ConfigLoadException(fileName, e.getMessage());
        }
        return config;
    }

    public void registerConfig(APIConfig APIConfig) {
        if(!APIConfigList.contains(APIConfig)){
            APIConfigList.add(APIConfig);
        }
    }

    void handleException(ConfigLoadException exception) {
        getLogger().warning(exception.getMessage());
    }
}
