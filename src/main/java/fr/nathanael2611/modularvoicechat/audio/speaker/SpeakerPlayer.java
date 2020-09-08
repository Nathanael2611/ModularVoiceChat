package fr.nathanael2611.modularvoicechat.audio.speaker;

import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Based on: https://github.com/MC-U-Team/Voice-Chat/blob/1.15.2/audio-client/src/main/java/info/u_team/voice_chat/audio_client/speaker/SpeakerPlayer.java
 */
public class SpeakerPlayer implements NoExceptionCloseable
{

    private final ExecutorService executor = Executors.newCachedThreadPool(ThreadUtil.createDaemonFactory("speaker player"));

    private final SpeakerData speakerData;

    private final Map<Integer, SpeakerBufferPusher> bufferMap;

    public SpeakerPlayer(SpeakerData speakerData)
    {
        this.speakerData = speakerData;
        bufferMap = new HashMap<>();
    }

    public void accept(int id, byte[] opusPacket, int volumePercent)
    {
        if (speakerData.isAvailable(id))
        {
            bufferMap.computeIfAbsent(id, $ -> new SpeakerBufferPusher(executor, id, speakerData)).decodePush(opusPacket, volumePercent);
        }
    }

    @Override
    public void close()
    {
        bufferMap.values().forEach(SpeakerBufferPusher::close);
        bufferMap.clear();
        executor.shutdown();
    }
}
