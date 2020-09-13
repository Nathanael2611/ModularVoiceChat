package fr.nathanael2611.modularvoicechat.client.voice.audio;

import com.google.gson.JsonPrimitive;
import fr.nathanael2611.modularvoicechat.api.StartVoiceRecordEvent;
import fr.nathanael2611.modularvoicechat.api.StopVoiceRecordEvent;
import fr.nathanael2611.modularvoicechat.api.VoiceProperties;
import fr.nathanael2611.modularvoicechat.audio.AudioTester;
import fr.nathanael2611.modularvoicechat.client.gui.GuiConfig;
import fr.nathanael2611.modularvoicechat.client.voice.VoiceClientManager;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceEndToServer;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceToServer;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.audio.micro.MicroData;
import fr.nathanael2611.modularvoicechat.audio.micro.MicroRecorder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class MicroHandler implements NoExceptionCloseable
{

    private ClientConfig config;

    private final MicroData data;
    private final MicroRecorder recorder;

    public MicroHandler()
    {
        this.config = ClientProxy.getConfig();
        this.data = new MicroData(this.config.get(ClientConfig.MICROPHONE).getAsString(), this.config.get(ClientConfig.MICROPHONE_VOLUME).getAsInt());
        this.recorder = new MicroRecorder(data, this::sendVoicePacket, config.get(ClientConfig.BITRATE).getAsInt());
    }

    private boolean lastAudioTesting = false;

    public void sendVoicePacket(byte[] opusPacket)
    {
        if (VoiceClientManager.isStarted())
        {
            if(GuiConfig.audioTesting && opusPacket != null)
            {
                AudioTester.speaker.receiveVoicePacket(0, opusPacket, 100, VoiceProperties.empty());
            }
            else if(this.lastAudioTesting || opusPacket == null)
            {
                AudioTester.speaker.receiveEnd(0);
                this.lastAudioTesting = false;
            }
            else
            {
                if(opusPacket == null)
                {
                    VoiceClientManager.getClient().send(new VoiceEndToServer());
                }
                else
                {
                    VoiceClientManager.getClient().send(new VoiceToServer(opusPacket));
                }
            }
        }
        this.lastAudioTesting = GuiConfig.audioTesting;
    }

    public void start()
    {
        if(Minecraft.getMinecraft().player != null)
        {
            MinecraftForge.EVENT_BUS.post(new StartVoiceRecordEvent());
        }
        recorder.start();
    }

    public void stop()
    {
        if(Minecraft.getMinecraft().player != null)
        {
            MinecraftForge.EVENT_BUS.post(new StopVoiceRecordEvent());
        }
        recorder.stop();
        if (!GuiConfig.audioTesting)
        {
            this.sendVoicePacket(null);
        }
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
