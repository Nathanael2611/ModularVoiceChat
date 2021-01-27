package fr.nathanael2611.modularvoicechat.client.voice.audio;

import com.google.gson.JsonPrimitive;
import fr.nathanael2611.modularvoicechat.api.VoiceProperties;
import fr.nathanael2611.modularvoicechat.client.SpeakingPlayers;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.audio.speaker.SpeakerData;
import fr.nathanael2611.modularvoicechat.audio.speaker.SpeakerPlayer;
import fr.nathanael2611.modularvoicechat.proxy.CommonProxy;
import fr.nathanael2611.modularvoicechat.util.Helpers;

public class SpeakerHandler implements NoExceptionCloseable
{

    private ClientConfig config;

    private final SpeakerData data;
    private final SpeakerPlayer player;

    public SpeakerHandler()
    {
        this.config = ClientProxy.getConfig();
        data = new SpeakerData(config.get(ClientConfig.SPEAKER).getAsString(), config.get(ClientConfig.SPEAKER_VOLUME).getAsInt());
        player = new SpeakerPlayer(data);
    }

    public void receiveVoicePacket(int id, byte[] opusPacket, int volumePercent, VoiceProperties properties)
    {
        String name = Helpers.clientGetPlayerNameForId(id);
        if(name == null || !CommonProxy.getMutedPlayers().isMuted(name))
        {
            player.accept(id, opusPacket, volumePercent, properties);
        }
        SpeakingPlayers.updateTalking(id);
    }

    public void receiveEnd(int id)
    {
        player.accept(id, null, 0, null);
    }


    public String getSpeaker()
    {
        return data.getMixer();
    }

    public void setSpeaker(String mixer)
    {
        this.data.setMixer(mixer);
        config.set(ClientConfig.SPEAKER, new JsonPrimitive(mixer));
    }

    public int getVolume()
    {
        return data.getVolume();
    }

    public void setVolume(int volume)
    {
        this.data.setVolume(volume);
        this.config.set(ClientConfig.SPEAKER_VOLUME, new JsonPrimitive(volume));
    }

    @Override
    public void close()
    {
        this.player.close();
        this.data.close();
    }

}
