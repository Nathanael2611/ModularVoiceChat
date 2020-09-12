package fr.nathanael2611.modularvoicechat.api;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event will be called after a player recorded a voice sample,
 * just before this sample be encoded and sent to the server.
 */
public class VoiceRecordedEvent extends Event
{

    /* The recorded audio-sample */
    private byte[] recordedSamples;

    /**
     * Constructor
     * @param recordedSamples the recorder audio-samples
     */
    public VoiceRecordedEvent(byte[] recordedSamples)
    {
        this.recordedSamples = recordedSamples;
    }

    /**
     * This event is cancelable
     * @return true
     */
    @Override
    public boolean isCancelable()
    {
        return true;
    }

    /**
     * Getter for recorded samples
     * @return the recorded samples
     */
    public byte[] getRecordedSamples()
    {
        return recordedSamples;
    }

    /**
     * Used to set the recorded samples to a new value
     * @param recordedSamples the new recorded samples
     */
    public void setRecordedSamples(byte[] recordedSamples)
    {
        this.recordedSamples = recordedSamples;
    }

}
