package fr.nathanael2611.modularvoicechat.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.api.VoiceProperties;
import fr.nathanael2611.modularvoicechat.network.objects.*;
import fr.nathanael2611.modularvoicechat.proxy.CommonProxy;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.command.CommandTitle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraftforge.common.MinecraftForge;

public class KryoNetServerListener extends Listener
{

    private VoiceServer voiceServer;

    KryoNetServerListener(VoiceServer server)
    {
        this.voiceServer = server;
    }

    @Override
    public void disconnected(Connection connection)
    {
        super.disconnected(connection);
        BiMap<Connection, Integer> map = this.voiceServer.CONNECTIONS_MAP.inverse();
        if(map.containsKey(connection))
        {
            this.voiceServer.CONNECTIONS_MAP.remove(map.get(connection));
        }
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
                if(!CommonProxy.getMutedPlayers().isMuted(player.getName()))
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
                else
                {
                    SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.ACTIONBAR,
                            new TextComponentTranslation("mvc.error.muted"),
                    1, 1, 1);
                    player.connection.sendPacket(spackettitle1);
                }
            }
            else if (object instanceof VoiceEndToServer)
            {
                this.voiceServer.sendToAllExcept(player, new VoiceEndToClient(player.getEntityId()));
            }
        }
        super.received(connection, object);
    }
}