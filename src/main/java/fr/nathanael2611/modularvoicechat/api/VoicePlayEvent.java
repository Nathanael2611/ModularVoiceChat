package fr.nathanael2611.modularvoicechat.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class VoicePlayEvent extends Event
{

    private byte[] recordedSamples;
    private int volumePercent;

    public VoicePlayEvent(byte[] recordedSamples, int volumePercent)
    {
        this.recordedSamples = recordedSamples;
        this.volumePercent = volumePercent;
    }

    @Override
    public boolean isCancelable()
    {
        return true;
    }

    public byte[] getRecordedSamples()
    {
        return recordedSamples;
    }

    public void setRecordedSamples(byte[] recordedSamples)
    {
        this.recordedSamples = recordedSamples;
    }

    public int getVolumePercent()
    {
        return volumePercent;
    }

    public void setVolumePercent(int volumePercent)
    {
        this.volumePercent = volumePercent;
    }

}
