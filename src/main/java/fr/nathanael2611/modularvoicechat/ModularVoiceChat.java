package fr.nathanael2611.modularvoicechat;

import fr.nathanael2611.modularvoicechat.network.vanilla.VanillaPacketHandler;
import fr.nathanael2611.modularvoicechat.proxy.CommonProxy;
import fr.nathanael2611.modularvoicechat.server.VoiceServerManager;
import fr.nathanael2611.modularvoicechat.server.command.VoiceMute;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.io.File;

@Mod(modid = ModularVoiceChat.MOD_ID, name = ModularVoiceChat.MOD_NAME)
public class ModularVoiceChat
{

    public static final String MOD_ID = "modularvc";
    public static final String MOD_NAME = "ModularVoiceChat";

    @Mod.Instance(MOD_ID)
    public static ModularVoiceChat INSTANCE;

    @SidedProxy(serverSide = "fr.nathanael2611.modularvoicechat.proxy.ServerProxy", clientSide = "fr.nathanael2611.modularvoicechat.proxy.ClientProxy")
    private static CommonProxy proxy;

    public static File modConfigDir;

    public static final int DEFAULT_PORT = 7656;
    public static final String DISCORD_INVITE = "https://discord.gg/kSu7eFE";

    @Mod.EventHandler
    public void onPreInitialization(FMLPreInitializationEvent event)
    {
        modConfigDir = new File(event.getModConfigurationDirectory(), String.format("/%s/", MOD_NAME));
        if(!modConfigDir.exists())
        {
            modConfigDir.mkdirs();
        }

        VanillaPacketHandler.getInstance().registerPackets();

        proxy.onPreInitialization(event);
    }

    @Mod.EventHandler
    public void onInitialization(FMLInitializationEvent event)
    {
        proxy.onInitialization(event);
    }

    @Mod.EventHandler
    public void onPostInitialization(FMLPostInitializationEvent event)
    {
        proxy.onPostInitialization(event);
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new VoiceMute());
        if(event.getServer().isDedicatedServer())
        {
            if(!VoiceServerManager.isStarted())
            {
                VoiceServerManager.start();
            }
        }
    }

    @Mod.EventHandler
    public void onServerStop(FMLServerStoppingEvent event)
    {
        if(VoiceServerManager.isStarted())
        {
            VoiceServerManager.stop();
        }
    }

}
