package io.github.geertbraakman;

import io.github.geertbraakman.api.APIPlugin;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyHandler extends Handler {

    private static List<PropertyHandler> instances;

    public static PropertyHandler getInstance(APIPlugin plugin) {
        if(instances == null) {
            instances = new ArrayList<>();
        }

        for (PropertyHandler handler: instances){
            if(handler.getAPIPlugin().equals(plugin)){
                return handler;
            }
        }

        PropertyHandler instance = new PropertyHandler(plugin);
        instances.add(instance);
        return instance;
    }

    private Properties properties;
    private final String filename;

    PropertyHandler(APIPlugin plugin){
        super(plugin);
        filename = "api.properties";
        try {
            initialiseFile();
        } catch (IOException e) {
            getLogger().warning("Could not load 'api.properties'. Message: " + e.getMessage());
        }
    }

    private void initialiseFile() throws IOException {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(filename));
    }

    public String getValue(String key) {
        String value = null;
        if(properties != null && key != null){
            value = properties.getProperty(key);
        }
        return value;
    }

    public String getValue(PropertyKey key) {
        String value = null;
        if (key != null) {
            value = getValue(key.getPath());
        }
        return value;
    }

    public boolean subCommandCheck() {
       String value =  getValue(PropertyKey.SUB_COMMAND_CHECK);
       if(value == null){
           return true; // This is the default!
       }
       return Boolean.parseBoolean(getValue(PropertyKey.SUB_COMMAND_CHECK));
    }

}
