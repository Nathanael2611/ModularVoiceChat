package fr.nathanael2611.modularvoicechat.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.network.objects.HelloImAPlayer;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceToServer;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class KryoNetServerListener extends Listener
{

    private VoiceServer voiceServer;

    KryoNetServerListener(VoiceServer server)
    {
        this.voiceServer = server;
    }

    @Override
    public void received(Connection connection, Object object)
    {
        EntityPlayerMP player = this.voiceServer.getPlayer(connection);
        if (object instanceof HelloImAPlayer)
        {
            HelloImAPlayer hello = ((HelloImAPlayer) object);
            Helpers.log("A new player tried to connect to VoiceServer named: " + hello.playerName);
            EntityPlayerMP playerMP = Helpers.getPlayerByUsername(hello.playerName);
            if (playerMP != null)
            {
                voiceServer.CONNECTIONS_MAP.put(playerMP.getEntityId(), connection);
                Helpers.log("Successfully added " + hello.playerName + " to voice-server connected-players!");
            }
            else
            {
                Helpers.log("No player named: " + hello.playerName);
            }
        } else if (player != null)
        {
            if (object instanceof VoiceToServer)
            {
                VoiceToServer voiceToServer = (VoiceToServer) object;
                VoiceDispatchEvent event = new VoiceDispatchEvent(voiceServer, player, voiceToServer.opusBytes);
                MinecraftForge.EVENT_BUS.post(event);
                if(!event.isCanceled())
                {
                    event.getVoiceServer().getVoiceDispatcher().dispatch(event);
                }
                event.finalizeDispatch();
            }
        }
        super.received(connection, object);
    }
}