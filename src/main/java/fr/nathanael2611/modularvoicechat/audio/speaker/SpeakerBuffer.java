package fr.nathanael2611.modularvoicechat.audio.speaker;

import fr.nathanael2611.modularvoicechat.api.VoiceProperties;

import java.util.concurrent.*;

/**
 *
 * Based on: https://github.com/MC-U-Team/Voice-Chat/blob/1.15.2/audio-client/src/main/java/info/u_team/voice_chat/audio_client/speaker/SpeakerBuffer.java
 */
public class SpeakerBuffer
{

    private final BlockingQueue<AudioEntry> queue;

    SpeakerBuffer(int size)
    {
        this.queue = new LinkedBlockingQueue<>(size);
    }

    public AudioEntry getNextPacket()
    {
        try
        {
            return queue.take();
        } catch (InterruptedException ex)
        {
            throw new AssertionError();
        }
    }

    public void pushPacket(byte[] packet, int volumePercent, VoiceProperties properties)
    {
        AudioEntry entry = new AudioEntry(packet, volumePercent, properties);
        if (!queue.offer(entry))
        {
            queue.poll();
            queue.offer(entry);
        }
    }

    static class AudioEntry
    {
        private byte[] packet;
        private int volumePercent;
        private VoiceProperties properties;

        AudioEntry(byte[] packet, int volumePercent, VoiceProperties properties)
        {
            this.packet = packet;
            this.volumePercent = volumePercent;
            this.properties = properties;
        }

        int getVolumePercent()
        {
            return volumePercent;
        }

        byte[] getPacket()
        {
            return packet;
        }

        VoiceProperties getProperties()
        {
            return properties;
        }
    }

}
