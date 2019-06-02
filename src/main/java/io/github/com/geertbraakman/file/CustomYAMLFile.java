package io.github.com.geertbraakman.file;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CustomYAMLFile {

    private String fileName;
    private FileConfiguration config;
    private File configFile;
    private Plugin plugin;
    private Logger logger;
    private String version;
    protected final char SPLITTER = '.';

    /**
     * Creates a new Instance of CustomYAMLFile
     *
     * @param plugin - The plugin that creates this config.
     * @param fileName - The name of the file.
     * @param latestVersion - The Latest version of the file
     */
    public CustomYAMLFile(Plugin plugin, String fileName, String latestVersion){
        this.plugin = plugin;
        setFileName(fileName);
        logger = plugin.getLogger();
        saveDefaultConfig();
        loadConfig();
        this.version = getConfig().getString("config-version");

        if(this.version == null || !this.version.equals(latestVersion)){
            updateConfig(latestVersion);
        }

    }

    private void updateConfig(String latestVersion){
        log(Level.WARNING, "the version of " + fileName + " is not the same as the version provided by the latest version of the plugin.");
        log(Level.WARNING, "installed version: " + version);
        log(Level.WARNING, "provided version: " + latestVersion);
        File path = new File(plugin.getDataFolder(), "deprecatedFiles");
        if(version == null){
            version = "0.0";
        }

        File oldFile = new File(path, "["+ version + "]" + fileName);
            try {
                config.save(oldFile);
                log(Level.INFO, "Created a copy of " + fileName + " at " + oldFile.getPath());
            } catch (IOException e) {
                log(Level.WARNING, "could not create a copy of " + fileName);
                log(Level.WARNING, e.getMessage());
            }
        saveLatest();
    }

    /**
     * Set the filename of this APIConfig
     *
     * @param name - the name of the file
     */
    private void setFileName(String name) {
        if (!name.endsWith(".yml")){
            name += ".yml";
        }
        this.fileName = name;
    }

    /**
     * Log to the console.
     *
     * @param level - The level you want to log.
     * @param msg - The msg you want to display
     */
    protected void log(Level level, String msg) {
        logger.log(level, msg);
    }

    /**
     * Load the config File.
     */
    private void loadConfigFile(){
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), fileName);
        }
    }

    /**
     * Saves the default config if it does not exists
     */
    public void saveDefaultConfig(){
        loadConfigFile();
        if (!configFile.exists()) {
            log(Level.INFO, fileName + " does not exists, creating it now.");
            plugin.saveResource(fileName, false);
        }
    }

    public void loadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);

//        // Look for defaults in the jar
//        Reader defConfigStream = new InputStreamReader(plugin.getResource(fileName), StandardCharsets.UTF_8);
//        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
//        config.setDefaults(defConfig);
        logger.log(Level.INFO, "loading " + fileName);
    }

    /**
     * Will save the latest version of the config file, please note that this will overwrite the current version of the file!
     */
    public void saveLatest() {
        plugin.saveResource(fileName, true);
    }

    public void saveConfig(){
        if(config == null || configFile == null){
            return;
        }
        try {
            config.save(configFile);
            log(Level.INFO, "Saved " + fileName);
        } catch (IOException e){
            log(Level.SEVERE, "Could not save " + fileName + " to " + configFile);
            log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Will reload the config.
     */
    public void reloadConfig(){
        loadConfigFile();
        loadConfig();
        log(Level.INFO, "Reloaded " + fileName);
    }

    /**
     * Gets the FileConfiguration that this class represents
     *
     * @return - the FileConfiguration from this class
     */
    public FileConfiguration getConfig(){
        if(config == null){
            loadConfig();
        }
        return config;
    }

    /**
     * Get the plugin that this class uses
     *
     * @return - the Plugin from this class
     */
    protected Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the name of the file
     *
     * @return String - Filename
     */
    public String getFileName(){
        return fileName;
    }

    /**
     * Get the version the file
     *
     * @return String - version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the version
     * @param version - The new version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
