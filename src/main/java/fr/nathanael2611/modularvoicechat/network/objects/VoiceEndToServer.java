package fr.nathanael2611.modularvoicechat.network.objects;

public class VoiceEndToServer
{
    public int entityId;

    public VoiceEndToServer(int entityId)
    {
        this.entityId = entityId;
    }

    /**
     * Constructor
     * Empty for serialization
     */
    public VoiceEndToServer()
    {
    }
}
