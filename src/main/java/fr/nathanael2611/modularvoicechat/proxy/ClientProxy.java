package fr.nathanael2611.modularvoicechat.proxy;

import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.audio.AudioTester;
import fr.nathanael2611.modularvoicechat.client.ClientEventHandler;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.server.command.VoiceMute;
import fr.nathanael2611.modularvoicechat.server.command.VoiceMuteClient;
import fr.nathanael2611.modularvoicechat.util.OpusLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.io.File;

public class ClientProxy extends CommonProxy
{

    public static final KeyBinding KEY_SPEAK = new KeyBinding(I18n.format("mvc.config.pushtotalk"), Keyboard.KEY_V, ModularVoiceChat.MOD_NAME);
    public static final KeyBinding KEY_OPEN_CONFIG = new KeyBinding(I18n.format("mvc.config.openconfig"), Keyboard.KEY_NONE, ModularVoiceChat.MOD_NAME);

    private static ClientConfig config;

    @Override
    public void onPreInitialization(FMLPreInitializationEvent event)
    {
        super.onPreInitialization(event);

        ClientRegistry.registerKeyBinding(KEY_SPEAK);
        ClientRegistry.registerKeyBinding(KEY_OPEN_CONFIG);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler(Minecraft.getMinecraft()));
        config = new ClientConfig(new File(ModularVoiceChat.modConfigDir, "/ClientConfig.json"));
        ClientCommandHandler.instance.registerCommand(new VoiceMuteClient());

        if(!OpusLoader.loadOpus())
        {
            JOptionPane.showMessageDialog(null, "\n" + "Opus initialization failed. ModularVoiceChat will not work.", "Opus initialization error", JOptionPane.ERROR_MESSAGE);
            FMLCommonHandler.instance().exitJava(0, true);
        }

        AudioTester.start();
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);
    }

    @Override
    public void onPostInitialization(FMLPostInitializationEvent event)
    {
        super.onPostInitialization(event);
    }

    public static ClientConfig getConfig()
    {
        return config;
    }
}
