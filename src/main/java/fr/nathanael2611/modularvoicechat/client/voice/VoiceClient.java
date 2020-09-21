package fr.nathanael2611.modularvoicechat.client.voice;

import com.esotericsoftware.kryonet.Client;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.network.objects.HelloImAPlayer;
import fr.nathanael2611.modularvoicechat.network.objects.KryoObjects;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The VoiceClient
 */
public class VoiceClient
{
    /* The server port */
    private int port;
    /* The server host */
    private String host;
    /* The Client */
    private Client client;
    /* Is handshake done */
    private boolean handshakeDone = false;

    private final ScheduledExecutorService RECONNECT_SERVICE = Executors.newSingleThreadScheduledExecutor();

    /**
     * Constructor
     * @param playerName the player name
     * @param port the server port
     */
    VoiceClient(String playerName, String host, int port)
    {
        this.port = port;
        this.host = host;
        this.client = new Client(10000000, 10000000);
        client.start();
        KryoObjects.registerObjects(client.getKryo());
        client.addListener(new KryoNetClientListener(this));
        RECONNECT_SERVICE.scheduleAtFixedRate(() -> {
            if(!this.client.isConnected() && host != null)
            {
                try
                {
                    Helpers.log(String.format("Try to connect to the UDP server! [%s:%s]", host, this.port));
                    client.connect(5000, host, port + 1, port);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§4[" + ModularVoiceChat.MOD_NAME + "] §cCannot connect to voice-server, try reconnecting! (or see logs for complete error)"));
                    Helpers.log("Failed to connect to VoiceServer.");
                }
            }
            else if(host == null)
            {
                Helpers.log("Host is null!");
            }
            else if(!isHandshakeDone())
            {
                {
                    Helpers.log("Try authenticate with username " + playerName);
                    this.authenticate(playerName);
                }
            }
        }, 1, 5, TimeUnit.SECONDS);


    }

    public void setHandshakeDone()
    {
        this.handshakeDone = true;
        Helpers.log("Successfully authenticate with " + Minecraft.getMinecraft().player.getName());
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§2[" + ModularVoiceChat.MOD_NAME + "] §aSuccessfully established connection with voice-server!"));
    }

    public boolean isHandshakeDone()
    {
        return handshakeDone;
    }

    /**
     * Send a packet to server
     * @param object packet
     */
    public void send(Object object)
    {
        this.client.sendUDP(object);
    }

    /**
     * Authenticate with a given name
     * @param name player name
     */
    private void authenticate(String name)
    {
        send(new HelloImAPlayer(name));
        System.out.println("Successfuly send");
    }

    /**
     * Close the VoiceClient
     */
    public void close()
    {
        this.client.close();
    }

    /**
     * Check if the connection is connected to server
     * @return true if client is connected
     */
    public boolean isConnected()
    {
        return this.client != null && this.client.isConnected();
    }

}
