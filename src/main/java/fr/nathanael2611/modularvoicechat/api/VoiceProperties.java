package fr.nathanael2611.modularvoicechat.api;

import com.google.common.collect.Maps;

import java.util.HashMap;

public class VoiceProperties
{

    public HashMap<String, Integer> properties = Maps.newHashMap();

    public VoiceProperties(HashMap<String, Integer> properties)
    {
        this.properties = properties;
    }

    public VoiceProperties()
    {
    }

    public boolean getBooleanValue(String key)
    {
        return this.getIntValue(key) > 0;
    }

    public int getIntValue(String key)
    {
        return this.properties.getOrDefault(key, 0);
    }

    public void setBooleanValue(String key, boolean flag)
    {
        this.setIntValue(key, flag ? 1 : 0);
    }

    public void setIntValue(String key, int value)
    {
        this.properties.put(key, value);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static VoiceProperties empty()
    {
        return new VoiceProperties();
    }

    public static class Builder
    {
        private VoiceProperties properties;

        private Builder()
        {
            this.properties = new VoiceProperties();
        }

        public Builder with(String key, int value)
        {
            this.properties.setIntValue(key, value);
            return this;
        }

        public Builder with(String key, boolean value)
        {
            this.properties.setBooleanValue(key, value);
            return this;
        }

        public VoiceProperties build()
        {
            return this.properties;
        }
    }

}
