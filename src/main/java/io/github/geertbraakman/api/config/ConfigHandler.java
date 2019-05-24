package io.github.geertbraakman.api.config;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.exceptions.ConfigLoadException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler extends Handler {

    private static ConfigHandler instance;
    private static final String EXTENSION = ".yml";

    public static ConfigHandler getInstance(Plugin plugin) {
        if(instance == null){
            instance = new ConfigHandler(plugin);
        }
        return instance;
    }

    private List<Config> configList;

    private ConfigHandler(Plugin plugin) {
        super(plugin);
        configList = new ArrayList<>();
    }

    @Override
    public boolean reload() {
        for(Config config: configList){
            if(!config.reload()){
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
            if (!file.getParentFile().mkdirs()){
                throw new ConfigLoadException(fileName, "Could not create the file");
            }
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

    public void registerConfig(Config config) {
        if(!configList.contains(config)){
            configList.add(config);
        }
    }

    void handleException(ConfigLoadException exception) {
        getLogger().warning(exception.getMessage());
    }
}
