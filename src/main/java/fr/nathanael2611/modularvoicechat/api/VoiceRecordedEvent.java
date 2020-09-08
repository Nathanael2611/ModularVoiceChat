package fr.nathanael2611.modularvoicechat.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class VoiceRecordedEvent extends Event
{

    private byte[] recordedSamples;

    public VoiceRecordedEvent(byte[] recordedSamples)
    {
        this.recordedSamples = recordedSamples;
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
}
