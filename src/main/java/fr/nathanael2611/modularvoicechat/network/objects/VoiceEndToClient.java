package fr.nathanael2611.modularvoicechat.network.objects;

import fr.nathanael2611.modularvoicechat.api.VoiceProperties;

/**
 * This object will be send from the server to the client,
 * containing a voice-data.
 */
public class VoiceEndToClient
{

    /* The speaking-entity id */
    public int entityId;

    public VoiceEndToClient(int entityId)
    {
        this.entityId = entityId;
    }

    /**
     * Constructor
     * Empty for serialization
     */
    public VoiceEndToClient()
    {
    }
}
