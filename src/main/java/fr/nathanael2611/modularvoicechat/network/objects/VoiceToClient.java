package fr.nathanael2611.modularvoicechat.network.objects;

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

    /**
     * Constructor
     * @param entityId Id of the speaking-entity
     * @param opusBytes Encoded voice samples
     * @param volumePercent Volume at that we want the samples to be played
     */
    public VoiceToClient(int entityId, byte[] opusBytes, int volumePercent)
    {
        this.entityId = entityId;
        this.opusBytes = opusBytes;
        this.volumePercent = volumePercent;
    }

    /**
     * Constructor
     * Empty for serialization
     */
    public VoiceToClient()
    {
    }

}
