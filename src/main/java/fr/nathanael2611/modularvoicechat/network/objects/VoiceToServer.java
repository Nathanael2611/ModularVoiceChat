package fr.nathanael2611.modularvoicechat.network.objects;

/**
 * This object will be send from the client to the server.
 * It contain voice-data, that the server will dispatch around all players.
 */
public class VoiceToServer
{

    /* Encoded voice samples */
    public byte[] opusBytes;

    /**
     * Constructor
     * @param opusBytes Encoded voice samples
     */
    public VoiceToServer(byte[] opusBytes)
    {
        this.opusBytes = opusBytes;
    }

    /**
     * Constructor
     * Empty for serialization
     */
    public VoiceToServer()
    {
    }

}
