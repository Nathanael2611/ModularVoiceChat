package fr.nathanael2611.modularvoicechat.audio.micro;

import fr.nathanael2611.modularvoicechat.api.VoiceRecordedEvent;
import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.audio.api.IAudioEncoder;
import fr.nathanael2611.modularvoicechat.audio.impl.OpusEncoder;
import fr.nathanael2611.modularvoicechat.util.*;
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

    private long lastDetectedTimeStamp = -1;
    private double minimumGettedSound = 1;
    private double lastDb = 0;

    private boolean isIncreasing(double actual)
    {
        return actual > lastDb;
    }

    private boolean canDetectVoice(byte[] samples)
    {
        if(true) return true;
        double minSound = 41.0d;
        long time = System.currentTimeMillis();
        double rms = AudioUtil.calculateRms(samples);
        //float rms = AudioUtil.getdB(samples);

        this.minimumGettedSound = Math.min(rms, this.minimumGettedSound);
        double rmsVal = rms + 100;
        boolean perfectDetect = rmsVal > minSound || isIncreasing(rmsVal);
        System.out.println(rmsVal);

        //System.out.println(decibelF + "cc wave");
        if(perfectDetect)
        {
            this.lastDetectedTimeStamp = time;
        }
        boolean timePermission = time - this.lastDetectedTimeStamp <= 500;
        if(!timePermission)
        {
            opusPacketConsumer.accept(null);
        }
        this.lastDb = rmsVal;
        double volPercent = (int) ((rmsVal / minSound) * this.microData.getVolume());
        samples = AudioUtil.adjustVolume(samples, (int) ((rmsVal / minSound) * this.microData.getVolume()));
        //this.microData.setVolume((int) ((rmsVal / minSound) * this.microData.getVolume()));
        return perfectDetect/* || timePermission*/;
    }

    /**
     * le paramètre gamma correspond à la valeur qu'on met en seuil (un pourcentage du bruit moyen de base) au cas où le bruit moyen est plus fort que le bruit courant.
     * 	le paramètre beta gère l'importance de la correction, plus il est élevé, plus on va "débruiter"
     * 	On remarque donc qu'il faut que alpha soit > 0, sinon on n'a que du bruit blanc.
     * 	Aussi, plus gamma est élevé plus le bruit blanc est audible.
     * 	On a donc intérêt à garder gamma assez bas(entre 0 et 0.5 par exemple). Au dessus de 1, on ajoute du bruit.
     * 	si beta vaut 0 on a un signal non filtré.
     *
     * 	Si beta est trop élevé on ne reconnait pas beaucoup.
     * 	Il faut donc trouver un équilibre entre beta et alpha.
     */
    private byte[] reduceNoise(byte[] samples)
    {
        double minSound = 41.0d;
        long time = System.currentTimeMillis();
        double rms = AudioUtil.calculateRms(samples);
        //float rms = AudioUtil.getdB(samples);

        this.minimumGettedSound = Math.min(rms, this.minimumGettedSound);
        double rmsVal = rms + 100;
        float volume = (float) ((((rmsVal / minSound)) * this.microData.getVolume()) / 100) - 0.2f;

        System.out.println(volume);
        samples = AudioUtil.adjustVolume(samples, volume);

        return samples;
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
                byte[] samples = microData.read(buffer);
                if(this.canDetectVoice(samples))
                {
                    //samples = reduceNoise(samples);
                    VoiceRecordedEvent event = new VoiceRecordedEvent(samples);
                    MinecraftForge.EVENT_BUS.post(event);
                    byte[] recordedSamples = event.getRecordedSamples();
                    if(!event.isCanceled())
                    {
                        opusPacketConsumer.accept(encoder.encode(recordedSamples));
                    }
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
