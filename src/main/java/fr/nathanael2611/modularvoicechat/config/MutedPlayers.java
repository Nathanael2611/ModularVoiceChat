package fr.nathanael2611.modularvoicechat.config;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Client config manager
 * Using a json because normally, the users won't have to manually edit this.
 */
public class MutedPlayers extends GameConfig
{

    public static final ConfigProperty MUTED_PLAYERS = new ConfigProperty("muted-players", new JsonArray());

    public MutedPlayers(File config)
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

    public boolean isMuted(String playerName)
    {
        for (JsonElement element : this.get(MUTED_PLAYERS).getAsJsonArray())
        {
            if(element.getAsString().equalsIgnoreCase(playerName))
            {
                return true;
            }
        }
        return false;
    }

    public void setMute(String playerName, boolean muted)
    {
        JsonArray array = this.get(MUTED_PLAYERS).getAsJsonArray();
        for (int i = 0; i < array.size(); i++)
        {
            JsonElement element = array.get(i);
            if(element.getAsString().equalsIgnoreCase(playerName))
            {
                array.remove(element);
            }
        }
        if(muted)
        {
            array.add(playerName);
        }
        try
        {
            save();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void read() throws IOException
    {
        super.read();
        this.initProperty(MUTED_PLAYERS);
    }

    public List<String> getNames()
    {
        List<String> list = Lists.newArrayList();
        for (JsonElement element : this.get(MUTED_PLAYERS).getAsJsonArray())
        {
            list.add(element.getAsString());
        }
        return list;
    }
}
