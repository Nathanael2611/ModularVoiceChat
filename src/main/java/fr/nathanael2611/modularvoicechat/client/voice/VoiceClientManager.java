package fr.nathanael2611.modularvoicechat.client.voice;

public class VoiceClientManager
{

    private static VoiceClient CLIENT;

    public static synchronized void start(String playerName, String host, int port)
    {
        CLIENT = new VoiceClient(playerName, host, port);
    }

    public static synchronized void stop()
    {
        if (CLIENT != null)
        {
            CLIENT.close();
            CLIENT = null;
        }
    }

    public static boolean isStarted()
    {
        return CLIENT != null;
    }

    public static VoiceClient getClient()
    {
        return CLIENT;
    }
}