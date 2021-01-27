package fr.nathanael2611.modularvoicechat.proxy;

import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.config.MutedPlayers;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy
{

    private static MutedPlayers mutedPlayers;

    public void onPreInitialization(FMLPreInitializationEvent event)
    {
        mutedPlayers = new MutedPlayers(new File(ModularVoiceChat.modConfigDir, "/MutedPlayers.json"));
    }

    public void onInitialization(FMLInitializationEvent event)
    {

    }

    public void onPostInitialization(FMLPostInitializationEvent event)
    {

    }


    public static MutedPlayers getMutedPlayers()
    {
        return mutedPlayers;
    }
}
