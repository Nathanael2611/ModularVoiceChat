package fr.nathanael2611.modularvoicechat.config;

import com.google.common.collect.Maps;
import com.google.gson.*;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will manage the game-config !
 */
public abstract class GameConfig
{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /* Just contains the config file as File object */
    private File configFile;

    /* Contains all the config values that has been read from the json config file */
    private final HashMap<String, JsonElement> CONFIG_VALUES = Maps.newHashMap();

    /**
     * Constructor
     */
    public GameConfig(File configFile)
    {
        this.configFile = configFile;
    }

    /**
     * Reading the config file to the Config System
     */
    public void read() throws IOException {
        /* if the config file is valid, read the config to the HashMap */
        if(isConfigValid())
        {
            CONFIG_VALUES.clear();
            JsonObject object = getConfigAsJsonObject();
            object.entrySet().forEach(entry -> CONFIG_VALUES.put(entry.getKey(), entry.getValue()));
        } else { /* else, just re-create the config-file by default, and try to re-read the config */
            configFile.delete();
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            writer.write("{}");
            writer.close();
            this.read();
        }

    }

    public void save() throws IOException
    {
        JsonObject object = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : CONFIG_VALUES.entrySet())
        {
            object.add(entry.getKey(), entry.getValue());
        }

        FileUtils.writeStringToFile(configFile, GSON.toJson(object));
    }

    /**
     * Returns true if the config file is valid
     */
    private boolean isConfigValid()
    {
        return this.configFile.exists() && Helpers.isValidJson(Helpers.readFileToString(this.configFile)) && getConfigAsJsonObject() != null;
    }

    /**
     * Getting the config file as an JsonObject
     */
    private JsonObject getConfigAsJsonObject()
    {
        return new JsonParser().parse(Helpers.readFileToString(this.configFile)).getAsJsonObject();
    }

    /**
     * Get a JsonElement from the loaded config, with a ConfigProperty, or returns the default ConfigProperty value
     * if the ConfigProperty was not set in the json config file before loading.
     */
    public JsonElement get(ConfigProperty prop)
    {
        return CONFIG_VALUES.getOrDefault(prop.getKey(), prop.getDefaultValue());
    }

    public void set(ConfigProperty prop, JsonElement element)
    {
        CONFIG_VALUES.put(prop.getKey(), element);
        try
        {
            save();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void initProperty(ConfigProperty prop)
    {
        if(!CONFIG_VALUES.containsKey(prop.getKey()))
        {
            this.set(prop, prop.getDefaultValue());
        }
    }

}
