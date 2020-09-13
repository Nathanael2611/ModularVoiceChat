package fr.nathanael2611.modularvoicechat.client.voice;

import com.esotericsoftware.kryonet.Client;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.network.objects.HelloImAPlayer;
import fr.nathanael2611.modularvoicechat.network.objects.KryoObjects;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

/**
 * The VoiceClient
 */
public class VoiceClient
{

    /* The server hostname */
    private String host;
    /* The server port */
    private int port;
    /* The Client */
    private Client client;

    /**
     * Constructor
     * @param playerName the player name
     * @param host the server hostname
     * @param port the server port
     */
    VoiceClient(String playerName, String host, int port)
    {
        this.host = host;
        this.port = port;

        Thread t = new Thread(() -> {
            this.client = new Client(10000000, 10000000);
            KryoObjects.registerObjects(client.getKryo());
            client.start();
            client.addListener(new KryoNetClientListener(this));
            {
                try
                {
                    client.connect(5000, host, port + 1, port);
                    if (VoiceClientManager.isStarted())
                    {
                        Helpers.log("Try authenticate with username " + playerName);
                        VoiceClientManager.getClient().authenticate(playerName);
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§2[" + ModularVoiceChat.MOD_NAME + "] §aSuccessfully established connection with voice-server!"));
                    }
                } catch (IOException e)
                {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§4[" + ModularVoiceChat.MOD_NAME + "] §cCannot connect to voice-server, try reconnecting! (or see logs for complete error)"));
                    e.printStackTrace();
                }
            }
        });
        t.setName("VoiceClient-Kryo");
        t.start();

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
