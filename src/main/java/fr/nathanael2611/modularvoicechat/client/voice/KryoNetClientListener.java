package fr.nathanael2611.modularvoicechat.client.voice;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceEndToClient;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceToClient;

public class KryoNetClientListener extends Listener
{

    private VoiceClient voiceClient;

    public KryoNetClientListener(VoiceClient kryoNetVoiceClient)
    {
        this.voiceClient = kryoNetVoiceClient;
    }

    @Override
    public void received(Connection connection, Object object)
    {
        if(SpeakerManager.isRunning())
        {
            if (object instanceof VoiceToClient)
            {
                VoiceToClient voiceToClient = (VoiceToClient) object;
                SpeakerManager.getHandler().receiveVoicePacket(voiceToClient.entityId, voiceToClient.opusBytes, voiceToClient.volumePercent, voiceToClient.properties);
            }
            else if(object instanceof VoiceEndToClient)
            {
                VoiceEndToClient voiceToClient = (VoiceEndToClient) object;
                SpeakerManager.getHandler().receiveEnd(voiceToClient.entityId);
            }
        }
        super.received(connection, object);
    }
}
