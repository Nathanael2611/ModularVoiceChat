package fr.nathanael2611.modularvoicechat.config;

import com.google.gson.JsonElement;

/**
 * A simple class that represent a config-property
 * Will be use to get some properties in the json config-file
 */
public class ConfigProperty
{

    /* The property key */
    private final String KEY;
    /* The default value, will be returned only if the key is not set in the json config-file */
    private final JsonElement DEFAULT_VALUE;

    /**
     * Constructor
     */
    ConfigProperty(String key, JsonElement defaultValue)
    {
        this.KEY = key;
        this.DEFAULT_VALUE = defaultValue;
    }

    /**
     * Just returns the property-key
     */
    public String getKey() {
        return KEY;
    }

    /**
     * Just returns the property default-value
     */
    public JsonElement getDefaultValue() {
        return DEFAULT_VALUE;
    }
}
