package fr.nathanael2611.modularvoicechat.audio.speaker;

import fr.nathanael2611.modularvoicechat.api.VoicePlayEvent;
import fr.nathanael2611.modularvoicechat.api.VoiceProperties;
import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.audio.api.IAudioDecoder;
import fr.nathanael2611.modularvoicechat.audio.impl.OpusDecoder;
import net.minecraftforge.common.MinecraftForge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 *
 * Based on: https://github.com/MC-U-Team/Voice-Chat/blob/1.15.2/audio-client/src/main/java/info/u_team/voice_chat/audio_client/speaker/SpeakerBufferPusher.java
 */
public class SpeakerBufferPusher implements NoExceptionCloseable
{

    private final SpeakerBuffer buffer;
    private final IAudioDecoder decoder;
    private final Future<?> future;

    public SpeakerBufferPusher(ExecutorService executor, int id, SpeakerData speakerData)
    {
        this.buffer = new SpeakerBuffer(10);
        this.decoder = new OpusDecoder(4800, 2, 20, 1000);
        this.future = executor.submit(() ->
        {
            while (!Thread.currentThread().isInterrupted())
            {
                if (speakerData.isAvailable(id) && speakerData.freeBuffer(id) > 0)
                {
                    SpeakerBuffer.AudioEntry entry = buffer.getNextPacket();

                    VoicePlayEvent event = new VoicePlayEvent(entry.getPacket(), entry.getVolumePercent(), entry.getProperties());
                    MinecraftForge.EVENT_BUS.post(event);
                    if(!event.isCanceled())
                    {
                        speakerData.write(id, event.getRecordedSamples(), event.getVolumePercent());
                    }
                }
            }
        });
    }

    public void decodePush(byte[] opusPacket, int volumePercent, VoiceProperties properties)
    {
        push(decoder.decoder(opusPacket), volumePercent, properties);
    }

    private void push(byte[] packet, int volumePercent, VoiceProperties properties)
    {
        buffer.pushPacket(packet, volumePercent, properties);
    }

    @Override
    public void close()
    {
        future.cancel(true);
        decoder.close();
    }

}
