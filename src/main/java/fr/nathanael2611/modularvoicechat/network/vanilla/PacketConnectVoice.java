package fr.nathanael2611.modularvoicechat.network.vanilla;

import fr.nathanael2611.modularvoicechat.client.ClientEventHandler;
import fr.nathanael2611.modularvoicechat.client.voice.VoiceClientManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.InetSocketAddress;

/**
 * This will be send from the server to the client,
 * for request it to connect to the VoiceServer bind to the given port
 */
public class PacketConnectVoice implements IMessage
{

    private String ip;
    /* VoiceServer port */
    private int port;
    /* Player to link name */
    private String playerName;
    /* Show who speak */
    private boolean showWhoSpeak;

    /**
     * Constructor
     * Empty for serialization
     */
    public PacketConnectVoice()
    {
    }

    /**
     * Constructor
     *
     * @param port VoiceServer port
     */
    public PacketConnectVoice(String ip, int port, String playerName, boolean showWhoSpeak)
    {
        this.ip = ip;
        this.port = port;
        this.playerName = playerName;
        this.showWhoSpeak = showWhoSpeak;
    }

    /**
     * Reading packet
     *
     * @param buf buf that contain the packet objects
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.ip = ByteBufUtils.readUTF8String(buf);
        this.port = buf.readInt();
        this.playerName = ByteBufUtils.readUTF8String(buf);
        this.showWhoSpeak = buf.readBoolean();
    }

    /**
     * Writing packet to ByteBuf
     *
     * @param buf buf to write on
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.ip);
        buf.writeInt(this.port);
        ByteBufUtils.writeUTF8String(buf, this.playerName);
        buf.writeBoolean(this.showWhoSpeak);
    }

    public static class Message implements IMessageHandler<PacketConnectVoice, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketConnectVoice message, MessageContext ctx)
        {
            if(ctx.getClientHandler().getNetworkManager().getRemoteAddress() instanceof InetSocketAddress)
            {
                InetSocketAddress address = (InetSocketAddress) ctx.getClientHandler().getNetworkManager().getRemoteAddress();
                String host = message.ip.length() > 0 ? message.ip : getHostName(address);
                Helpers.log("Receiving voice-connect packet from server: " + host);
                new Thread(() ->
                {
                    try
                    {
                        Thread.sleep(2000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    Helpers.log("Connected to a Minecraft Server, trying to handle voice connection.");
                    if (VoiceClientManager.isStarted()) VoiceClientManager.stop();
                    if (MicroManager.isRunning()) MicroManager.stop();
                    if (SpeakerManager.isRunning()) SpeakerManager.stop();
                    Helpers.log("[pre] Handle VoiceClient start.");
                    VoiceClientManager.start(message.playerName, host, message.port);
                    MicroManager.start();
                    SpeakerManager.start();
                    ClientEventHandler.showWhoSpeak = message.showWhoSpeak;
                }).start();

            }
            return null;
        }
    }

    public static String getHostName(InetSocketAddress rescue)
    {
        ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        if(serverData != null)
        {
            return serverData.serverIP.split(":")[0];
        }
        else
        {
            String host = rescue.getHostName();
            if(host.endsWith("."))
            {
                host = host.substring(0, host.length() - 1);
            }
            return host;
        }
    }

}
