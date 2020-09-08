package fr.nathanael2611.modularvoicechat.server.dispatcher;

import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.entity.player.EntityPlayerMP;

public class DistanceBasedVoiceDispatcher implements IVoiceDispatcher
{

    private int maxDistance;
    private boolean fadeOut;

    public DistanceBasedVoiceDispatcher(int maxDistance, boolean fadeOut)
    {
        this.maxDistance = maxDistance;
        this.fadeOut = fadeOut;
    }

    @Override
    public void dispatch(VoiceDispatchEvent event)
    {
        for (EntityPlayerMP connectedPlayer : event.getVoiceServer().getConnectedPlayers())
        {
            if (connectedPlayer != event.getSpeaker())
            {
                double distance = event.getSpeaker().getDistance(connectedPlayer);
                if (distance <= this.maxDistance)
                {
                    int volume = this.fadeOut ? 100 - (int) Helpers.getPercent(distance, this.maxDistance) : 100;
                    event.dispatchTo(connectedPlayer, volume);
                }
            }
        }
    }
}
