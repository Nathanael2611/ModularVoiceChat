package fr.nathanael2611.modularvoicechat.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.api.VoiceProperties;
import fr.nathanael2611.modularvoicechat.network.objects.*;
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
                voiceServer.CONNECTIONS_MAP.remove(playerMP.getEntityId());
                voiceServer.CONNECTIONS_MAP.put(playerMP.getEntityId(), connection);
                Helpers.log("Successfully added " + hello.playerName + " to voice-server connected-players!");
                connection.sendTCP(new HelloYouAreAPlayer());
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
                VoiceDispatchEvent event = new VoiceDispatchEvent(voiceServer, player, voiceToServer.opusBytes, VoiceProperties.empty());
                MinecraftForge.EVENT_BUS.post(event);
                if(!event.isCanceled())
                {
                    event.getVoiceServer().getVoiceDispatcher().dispatch(event);
                }
                event.finalizeDispatch();
            }
            else if (object instanceof VoiceEndToServer)
            {
                this.voiceServer.sendToAllExcept(player, new VoiceEndToClient(player.getEntityId()));
            }
        }
        super.received(connection, object);
    }

    @Override
    public void disconnected(Connection connection) {
        if(voiceServer.CONNECTIONS_MAP.inverse().containsKey(connection)){
            voiceServer.CONNECTIONS_MAP.remove(connection.getID());
        }
    }

}