package io.github.geertbraakman.api.config;

import io.github.com.geertbraakman.Handler;
import io.github.com.geertbraakman.exceptions.ConfigLoadException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler extends Handler {

    private static final String EXTENSION = ".yml";

    private static List<ConfigHandler> instances;

    public static ConfigHandler getInstance(Plugin plugin)
    {
        if (instances == null) {
            instances = new ArrayList<>();
        }

        for(ConfigHandler instance: instances){
            if(instance.getPlugin().equals(plugin)){
                return  instance;
            }
        }

        ConfigHandler instance = new ConfigHandler(plugin);
        instances.add(instance);
        return instance;
    }

    private List<APIConfig> APIConfigList;

    private ConfigHandler(Plugin plugin) {
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
        File file = new File(getPlugin().getDataFolder(), fileName);
        if (!file.exists()) {
//            if (!file.getParentFile().mkdirs()){
//                throw new ConfigLoadException(fileName, "Could not create the file");
//            }
            file.getParentFile().mkdirs();
            getPlugin().saveResource(fileName, false);
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
