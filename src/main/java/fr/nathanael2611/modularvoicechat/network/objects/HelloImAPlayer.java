package fr.nathanael2611.modularvoicechat.network.objects;

/**
 * This object will be send from client to the server to inform it that a new player is connected to
 * the voice-server.
 */
public class HelloImAPlayer
{

    /**
     * The player-name.
     */
    public String playerName;

    /**
     * Constructor
     * @param playerName the name of the player that will be assigned to the connection.
     */
    public HelloImAPlayer(String playerName)
    {
        this.playerName = playerName;
    }

    /**
     * Constructor
     * Empty for serialization
     */
    public HelloImAPlayer()
    {
    }

}
