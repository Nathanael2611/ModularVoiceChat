package fr.nathanael2611.modularvoicechat.server;

import fr.nathanael2611.modularvoicechat.config.ServerConfig;
import fr.nathanael2611.modularvoicechat.network.vanilla.PacketConnectVoice;
import fr.nathanael2611.modularvoicechat.network.vanilla.VanillaPacketHandler;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class ServerEventHandler
{

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (VoiceServerManager.isStarted())
        {
            Helpers.log("Requesting " + event.player.getName() + " to connect to voice-server... Sending packet.");
            VanillaPacketHandler.getInstance().getNetwork().sendTo(new PacketConnectVoice(ServerConfig.generalConfig.forcedHostname, VoiceServerManager.getServer().getPort(), event.player.getName(), ServerConfig.generalConfig.showWhoSpeak), Helpers.getPlayerMP(event.player));
        }

    }



}
