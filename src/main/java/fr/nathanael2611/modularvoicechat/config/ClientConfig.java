package fr.nathanael2611.modularvoicechat.config;

import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.IOException;

public class ClientConfig extends GameConfig
{

    public static final ConfigProperty MICROPHONE = new ConfigProperty("microphone", new JsonPrimitive(""));
    public static final ConfigProperty MICROPHONE_VOLUME = new ConfigProperty("microphoneVolume", new JsonPrimitive(100));
    public static final ConfigProperty SPEAKER = new ConfigProperty("speaker", new JsonPrimitive(""));
    public static final ConfigProperty SPEAKER_VOLUME = new ConfigProperty("speakerVolume", new JsonPrimitive(100));
    public static final ConfigProperty BITRATE = new ConfigProperty("bitrate", new JsonPrimitive(96000));

    public ClientConfig(File config)
    {
        super(config);
        if(!config.exists())
        {
            try
            {
                config.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            read();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void read() throws IOException
    {
        super.read();
        this.initProperty(MICROPHONE);
        this.initProperty(MICROPHONE_VOLUME);
        this.initProperty(SPEAKER);
        this.initProperty(SPEAKER_VOLUME);
        this.initProperty(BITRATE);
    }
}
