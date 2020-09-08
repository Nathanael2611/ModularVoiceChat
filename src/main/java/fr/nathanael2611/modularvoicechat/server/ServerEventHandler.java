package fr.nathanael2611.modularvoicechat.server;

import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.network.vanilla.PacketConnectVoice;
import fr.nathanael2611.modularvoicechat.network.vanilla.VanillaPacketHandler;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ServerEventHandler
{

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(VoiceServerManager.isStarted())
        {
            Helpers.log("Requesting " + event.player.getName() + " to connect to voice-server.");
            VanillaPacketHandler.getInstance().getNetwork().sendTo(new PacketConnectVoice(VoiceServerManager.getServer().getPort(), event.player.getName()), Helpers.getPlayerMP(event.player));
        }
    }

    @SubscribeEvent
    public void onVoiceDispatch(VoiceDispatchEvent event)
    {
        for (EntityPlayerMP connectedPlayer : event.getVoiceServer().getConnectedPlayers())
        {
            if(connectedPlayer != event.getSpeaker())
            {
                int maxDistance = 15;
                double distance = event.getSpeaker().getDistance(connectedPlayer);
                if(distance < maxDistance)
                {
                    int volume = 100 - (int) Helpers.getPercent(distance, maxDistance);
                    event.dispatchTo(connectedPlayer, volume);
                }
            }
        }
    }

}
