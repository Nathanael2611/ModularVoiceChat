package fr.nathanael2611.modularvoicechat.audio;

import fr.nathanael2611.modularvoicechat.client.gui.GuiConfig;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerHandler;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;

public class AudioTester
{

    public static SpeakerHandler speaker;

    public static void start()
    {
        speaker = new SpeakerHandler();
    }

    public static void enable()
    {
        if (MicroManager.isRunning() && !MicroManager.getHandler().isSending())
        {
            MicroManager.getHandler().start();
        }
    }

    public static void disable()
    {
        if (MicroManager.isRunning() && MicroManager.getHandler().isSending())
        {
            MicroManager.getHandler().stop();
        }
    }

    public static void updateTester()
    {
        speaker.setSpeaker(SpeakerManager.getHandler().getSpeaker());
        if(GuiConfig.audioTesting)
        {
            enable();
        }
        else
        {
            disable();
        }
    }

}
