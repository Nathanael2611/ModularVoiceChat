package fr.nathanael2611.modularvoicechat.client.voice.audio;

import com.google.gson.JsonPrimitive;
import fr.nathanael2611.modularvoicechat.client.voice.VoiceClientManager;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceToServer;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.audio.micro.MicroData;
import fr.nathanael2611.modularvoicechat.audio.micro.MicroRecorder;

public class MicroHandler implements NoExceptionCloseable
{

    private ClientConfig config;

    private final MicroData data;
    private final MicroRecorder recorder;

    MicroHandler()
    {
        this.config = ClientProxy.getConfig();
        this.data = new MicroData(this.config.get(ClientConfig.MICROPHONE).getAsString(), this.config.get(ClientConfig.MICROPHONE_VOLUME).getAsInt());
        this.recorder = new MicroRecorder(data, this::sendVoicePacket, config.get(ClientConfig.BITRATE).getAsInt());
    }

    private void sendVoicePacket(byte[] opusPacket)
    {
        if (VoiceClientManager.isStarted())
        {
            VoiceClientManager.getClient().send(new VoiceToServer(opusPacket));
        }
    }

    public void start()
    {
        recorder.start();
    }

    public void stop()
    {
        recorder.stop();
    }

    public boolean isSending()
    {
        return recorder.isSending();
    }

    public String getMicro()
    {
        return data.getMixer();
    }

    public void setMicro(String mixer)
    {
        this.data.setMixer(mixer);
        this.config.set(ClientConfig.MICROPHONE, new JsonPrimitive(mixer));
    }

    public int getVolume()
    {
        return data.getVolume();
    }

    public void setVolume(int volume)
    {
        this.data.setVolume(volume);
        this.config.set(ClientConfig.MICROPHONE_VOLUME, new JsonPrimitive(volume));
    }

    @Override
    public void close()
    {
        this.recorder.close();
        this.data.close();
    }

}
