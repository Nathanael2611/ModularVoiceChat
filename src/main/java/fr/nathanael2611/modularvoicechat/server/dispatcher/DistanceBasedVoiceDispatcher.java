package fr.nathanael2611.modularvoicechat.server.dispatcher;

import fr.nathanael2611.modularvoicechat.api.HearDistanceEvent;
import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class DistanceBasedVoiceDispatcher implements IVoiceDispatcher
{

    private final int MAX_DISTANCE;
    private boolean fadeOut;

    public DistanceBasedVoiceDispatcher(int maxDistance, boolean fadeOut)
    {
        this.MAX_DISTANCE = maxDistance;
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
                double maxDistance = this.getHearDistance(event.getSpeaker(), connectedPlayer);
                if (distance <= maxDistance)
                {
                    int volume = this.fadeOut ? 100 - (int) Helpers.getPercent(distance, maxDistance) : 100;
                    event.dispatchTo(connectedPlayer, volume, event.getProperties());
                }
            }
        }
    }

    public double getHearDistance(EntityPlayerMP speaker, EntityPlayerMP hearer)
    {
        HearDistanceEvent hearDistanceEvent = new HearDistanceEvent(speaker, hearer, this.MAX_DISTANCE);
        MinecraftForge.EVENT_BUS.post(hearDistanceEvent);
        return hearDistanceEvent.getDistance();
    }

}
