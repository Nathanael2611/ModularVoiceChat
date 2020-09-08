package fr.nathanael2611.modularvoicechat.audio.micro;

import fr.nathanael2611.modularvoicechat.api.VoiceRecordedEvent;
import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.audio.api.IAudioEncoder;
import fr.nathanael2611.modularvoicechat.audio.impl.OpusEncoder;
import fr.nathanael2611.modularvoicechat.util.ThreadUtil;
import net.minecraftforge.common.MinecraftForge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 *
 * Based on: https://github.com/MC-U-Team/Voice-Chat/blob/1.15.2/audio-client/src/main/java/info/u_team/voice_chat/audio_client/micro/MicroRecorder.java
 */
public class MicroRecorder implements NoExceptionCloseable
{

    private final ExecutorService executor = Executors.newSingleThreadExecutor(ThreadUtil.createDaemonFactory("micro recorder"));

    private final MicroData microData;
    private final Consumer<byte[]> opusPacketConsumer;
    private final IAudioEncoder encoder;

    private volatile boolean send;

    public MicroRecorder(MicroData microData, Consumer<byte[]> opusPacketConsumer, int bitrate)
    {
        this.microData = microData;
        this.opusPacketConsumer = opusPacketConsumer;

        //this.encoder = new NoneEncoder();
        this.encoder = new OpusEncoder(48000, 2, 20, bitrate, 0, 1000);
    }

    public void start()
    {
        if (send || !microData.isAvailable())
        {
            return;
        }
        send = true;
        executor.execute(() ->
        {
            final byte[] buffer = new byte[960 * 2 * 2];
            while (send && microData.isAvailable())
            {
                VoiceRecordedEvent event = new VoiceRecordedEvent(microData.read(buffer));
                MinecraftForge.EVENT_BUS.post(event);
                byte[] recordedSamples = event.getRecordedSamples();
                if(!event.isCanceled())
                {
                    opusPacketConsumer.accept(encoder.encode(recordedSamples));
                }
            }
            ThreadUtil.execute(5, 20, () -> opusPacketConsumer.accept(encoder.silence()));
        });
    }

    public void stop()
    {
        send = false;
        microData.flush();
    }

    public boolean isSending()
    {
        return send;
    }

    @Override
    public void close()
    {
        executor.shutdown();
        encoder.close();
    }
}
