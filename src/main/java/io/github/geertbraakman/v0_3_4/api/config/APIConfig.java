package io.github.geertbraakman.api.config;

import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.exceptions.ConfigLoadException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * This Class is representing a fileConfiguration file, you throw in a filename and it will automatically load it from a yml file.
 *
 * The file has to be in the plugin, otherwise it will not work.
 */
public class APIConfig {

  private String fileName;
  private FileConfiguration fileConfiguration;
  private ConfigHandler handler;

    /**
     * The constructor will load the file, after creating it it's directly usable.
     * @param plugin The plugin you want to create this fileConfiguration with.
     * @param name - The name of the file, it will automatically put .yml behind it when you forget.
     */
  public APIConfig(APIPlugin plugin, String name) {
    this.fileName = ConfigHandler.addFileExtension(name);
    handler = plugin.getConfigHandler();
    reload();
  }

    /**
     * Will reload the fileConfiguration
     * @return if the reload passed.
     */
  boolean reload() {
      return (load() && onReload());
  }

    /**
     * Methode that can be overwritten by subclasses. Will be called by every load of the file.
     * @return if the reload passed.
     */
  public boolean onReload() {
    return true;
  }

    /**
     * This will load the file so it is usable;
     * @return if the load passed.
     */
  public boolean load() {
      try {
          fileConfiguration = handler.loadConfig(fileName);
      } catch (ConfigLoadException e) {
          handler.handleException(e);
          return false;
      }
      return true;
  }

    /**
     *
     * @return the fileConfiguration.
     */
  public FileConfiguration getFileConfiguration() {
    return this.fileConfiguration;
  }

    /**
     *
     * @return the file name
     */
  public String getFileName() {
    return fileName;
  }
}
