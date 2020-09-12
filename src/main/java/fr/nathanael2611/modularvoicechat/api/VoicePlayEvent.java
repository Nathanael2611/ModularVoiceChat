package fr.nathanael2611.modularvoicechat.api;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event will be called after a player receive and decoded a voice-packet,
 * just before this voice-packet will be played.
 */
public class VoicePlayEvent extends Event
{

    /* The recorded audio-sample */
    private byte[] audioSamples;
    /* The volume that we want the samples to be played */
    private int volumePercent;
    /* The audio assigned properties */
    private VoiceProperties properties;

    /**
     * Constructor
     * @param audioSamples audio-samples that will be played
     * @param volumePercent volume that we want the samples to be played
     * @param properties the audio assigned properties
     */
    public VoicePlayEvent(byte[] audioSamples, int volumePercent, VoiceProperties properties)
    {
        this.audioSamples = audioSamples;
        this.volumePercent = volumePercent;
        this.properties = properties;
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
     * Getter for the audio samples
     * @return the audio samples
     */
    public byte[] getAudioSamples()
    {
        return audioSamples;
    }

    /**
     * Used to set audio samples to a new value
     * @param audioSamples new audio samples
     */
    public void setAudioSamples(byte[] audioSamples)
    {
        this.audioSamples = audioSamples;
    }

    /**
     * Getter for volume
     * @return the volume percent
     */
    public int getVolumePercent()
    {
        return volumePercent;
    }

    /**
     * Used for set the volume to a new value
     * @param volumePercent new volume percent
     */
    public void setVolumePercent(int volumePercent)
    {
        this.volumePercent = volumePercent;
    }

    /**
     * Getter for voice properties
     * @return the voice properties
     */
    public VoiceProperties getProperties()
    {
        return properties;
    }
}
