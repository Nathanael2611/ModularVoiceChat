package fr.nathanael2611.modularvoicechat.network.objects;

import fr.nathanael2611.modularvoicechat.api.VoiceProperties;

/**
 * This object will be send from the server to the client,
 * containing a voice-data.
 */
public class VoiceToClient
{

    /* The speaking-entity id */
    public int entityId;
    /* Encoded voice samples */
    public byte[] opusBytes;
    /* The volume that we want samples to be played on the client */
    public int volumePercent;
    /* The voice-properties */
    public VoiceProperties properties;

    /**
     * Constructor
     * @param entityId Id of the speaking-entity
     * @param opusBytes Encoded voice samples
     * @param volumePercent Volume at that we want the samples to be played
     */
    public VoiceToClient(int entityId, byte[] opusBytes, int volumePercent, VoiceProperties properties)
    {
        this.entityId = entityId;
        this.opusBytes = opusBytes;
        this.volumePercent = volumePercent;
        this.properties = properties;
    }

    /**
     * Constructor
     * Empty for serialization
     */
    public VoiceToClient()
    {
    }

}
