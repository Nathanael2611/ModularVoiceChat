package fr.nathanael2611.modularvoicechat.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class HearDistanceEvent extends Event
{

    private EntityPlayerMP speaker;
    private EntityPlayerMP hearer;
    private double distance;

    public HearDistanceEvent(EntityPlayerMP speaker, EntityPlayerMP hearer, double distance)
    {
        this.speaker = speaker;
        this.hearer = hearer;
        this.distance = distance;
    }

    public EntityPlayerMP getSpeaker()
    {
        return speaker;
    }

    public EntityPlayerMP getHearer()
    {
        return hearer;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    @Override
    public boolean isCancelable()
    {
        return false;
    }
}
