package fr.nathanael2611.modularvoicechat.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class VoicePlayEvent extends Event
{

    private byte[] recordedSamples;
    private int volumePercent;
    private VoiceProperties properties;

    public VoicePlayEvent(byte[] recordedSamples, int volumePercent, VoiceProperties properties)
    {
        this.recordedSamples = recordedSamples;
        this.volumePercent = volumePercent;
        this.properties = properties;
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

    public VoiceProperties getProperties()
    {
        return properties;
    }
}
