package fr.nathanael2611.modularvoicechat.client.voice;

import com.esotericsoftware.kryonet.Client;
import fr.nathanael2611.modularvoicechat.network.objects.HelloImAPlayer;
import fr.nathanael2611.modularvoicechat.network.objects.KryoObjects;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.client.Minecraft;

import java.io.IOException;

public class VoiceClient
{

    private String host;
    private int port;
    private Client client;

    public VoiceClient(String playerName, String host, int port)
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
                    client.connect(5000, host, port);
                    if (VoiceClientManager.isStarted())
                    {
                        Helpers.log("Try authenticate with username " + playerName);
                        VoiceClientManager.getClient().authenticate(playerName);
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.setName("VoiceClient-Kryo");
        t.start();

    }

    public void send(Object object)
    {
        this.client.sendTCP(object);
    }

    public void authenticate(String name)
    {
        send(new HelloImAPlayer(name));
    }

    public void close()
    {
        this.client.close();
    }

}
