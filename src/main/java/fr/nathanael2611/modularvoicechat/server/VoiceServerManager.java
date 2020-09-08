package fr.nathanael2611.modularvoicechat.server;

import fr.nathanael2611.modularvoicechat.util.Helpers;

/**
 * Static VoiceServer manager
 * Used for simply access to the VoiceServer
 */
public class VoiceServerManager
{

    /* VoiceServer instance */
    private static VoiceServer INSTANCE;

    /**
     * Used to start the VoiceServer
     */
    public synchronized static void start()
    {
        Helpers.log("Starting VoiceServer...");
        INSTANCE = new VoiceServer();
    }

    /**
     * Used for stop the VoiceServer
     */
    public synchronized static void stop()
    {
        Helpers.log("Stopping VoiceServer...");
        if (INSTANCE != null)
        {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    /**
     * Used for check if VoiceServer is actually running
     * @return true if server is started
     */
    public static boolean isStarted()
    {
        return INSTANCE != null;
    }

    /**
     * VoiceServer getter
     * @return the VoiceServer instance
     */
    public static VoiceServer getServer()
    {
        return INSTANCE;
    }

}