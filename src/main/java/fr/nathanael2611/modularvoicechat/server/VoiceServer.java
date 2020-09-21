package fr.nathanael2611.modularvoicechat.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.api.VoiceServerStartEvent;
import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.config.ServerConfig;
import fr.nathanael2611.modularvoicechat.network.objects.HelloImAPlayer;
import fr.nathanael2611.modularvoicechat.network.objects.KryoObjects;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceToClient;
import fr.nathanael2611.modularvoicechat.network.objects.VoiceToServer;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VoiceServer
{

    final HashBiMap<Integer, Connection> CONNECTIONS_MAP = HashBiMap.create();

    private IVoiceDispatcher dispatcher;
    private int port;
    private Server server;

    VoiceServer()
    {
        this.port = ServerConfig.generalConfig.port;
        this.dispatcher = ServerConfig.generalConfig.dispatcher.createDispatcher();
        {
            VoiceServerStartEvent event = new VoiceServerStartEvent(this, this.dispatcher);
            MinecraftForge.EVENT_BUS.post(event);
            if(event.getVoiceDispatcher() != this.dispatcher)
            {
                this.dispatcher = event.getVoiceDispatcher();
            }
        }
        this.server = new Server(10000000, 10000000);
        KryoObjects.registerObjects(this.server.getKryo());
        server.start();
        try
        {
            server.bind(this.port + 1, this.port );
            server.addListener(new KryoNetServerListener(this));
            Helpers.log("Successfully started VoiceServer.");
        } catch (IOException e)
        {
            e.printStackTrace();
            Helpers.log("Failed to start VoiceServer.");
        }
    }

    public void sendToAllExcept(EntityPlayerMP except, Object obj)
    {
        for (Map.Entry<Integer, Connection> connectionsEntry : this.CONNECTIONS_MAP.entrySet())
        {
            if(except == null || connectionsEntry.getKey() != except.getEntityId())
            {
                if(connectionsEntry.getValue() != null)
                {
                    connectionsEntry.getValue().sendUDP(obj);
                }
            }
        }
    }

    public void send(EntityPlayerMP dest, Object obj)
    {
        Connection connection = getPlayerConnection(dest);
        if(connection != null)
        {
            connection.sendUDP(obj);
        }
    }

    public boolean isPlayerConnected(EntityPlayer player)
    {
        return getPlayerConnection(player)!= null;
    }

    public Connection getPlayerConnection(EntityPlayer player)
    {
        return player == null ? null : this.CONNECTIONS_MAP.get(player.getEntityId());
    }

    public boolean hasAssignedPlayer(Connection connection)
    {
        return getPlayer(connection) != null;
    }

    public EntityPlayerMP getPlayer(Connection connection)
    {
        return Helpers.getPlayerByEntityId(this.CONNECTIONS_MAP.inverse().getOrDefault(connection, -1));
    }

    public void close()
    {
        this.server.close();
        Helpers.log("Successfully closed VoiceServer.");
    }

    public int getPort()
    {
        return port;
    }

    public List<EntityPlayerMP> getConnectedPlayers()
    {
        List<EntityPlayerMP> list = Lists.newArrayList();
        for (Map.Entry<Connection, Integer> entry : this.CONNECTIONS_MAP.inverse().entrySet())
        {
            EntityPlayerMP player = Helpers.getPlayerByEntityId(entry.getValue());
            Connection conn = entry.getKey();
            if(player != null && conn != null && conn.isConnected())
            {
                list.add(player);
            }
        }
        return list;
    }

    public IVoiceDispatcher getVoiceDispatcher()
    {
        return dispatcher;
    }
}
