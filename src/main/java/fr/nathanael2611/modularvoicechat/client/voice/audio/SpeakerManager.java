package fr.nathanael2611.modularvoicechat.client.voice.audio;

public class SpeakerManager
{

    private static SpeakerHandler SPEAKER;

    public synchronized static void start()
    {
        SPEAKER = new SpeakerHandler();
    }

    public synchronized static void stop()
    {
        if (SPEAKER != null)
        {
            SPEAKER.close();
            SPEAKER = null;
        }
    }

    public static boolean isRunning()
    {
        return SPEAKER != null;
    }

    public static SpeakerHandler getHandler()
    {
        return SPEAKER;
    }

}
