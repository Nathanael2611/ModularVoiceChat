package fr.nathanael2611.modularvoicechat.network.vanilla;

import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.client.voice.VoiceClientManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This will be send from the server to the client,
 * for request it to connect to the VoiceServer bind to the given port
 */
public class PacketConnectVoice implements IMessage
{

    /* VoiceServer port */
    private int port;
    /* Player to link name */
    private String playerName;

    /**
     * Constructor
     * Empty for serialization
     */
    public PacketConnectVoice()
    {
    }

    /**
     * Constructor
     * @param port VoiceServer port
     */
    public PacketConnectVoice(int port, String playerName)
    {
        this.port = port;
        this.playerName = playerName;
    }

    /**
     * Reading packet
     * @param buf buf that contain the packet objects
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.port = buf.readInt();
        this.playerName = ByteBufUtils.readUTF8String(buf);
    }

    /**
     * Writing packet to ByteBuf
     * @param buf buf to write on
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.port);
        ByteBufUtils.writeUTF8String(buf, this.playerName);
    }

    public static class Message implements IMessageHandler<PacketConnectVoice, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketConnectVoice message, MessageContext ctx)
        {
            if (Minecraft.getMinecraft().getCurrentServerData() != null && !Minecraft.getMinecraft().getCurrentServerData().isOnLAN())
            {
                if (VoiceClientManager.isStarted())
                {
                    VoiceClientManager.stop();
                }
                if (MicroManager.isRunning())
                {
                    MicroManager.stop();
                }
                if (SpeakerManager.isRunning())
                {
                    SpeakerManager.stop();
                }
                VoiceClientManager.start(message.playerName, Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":")[0], message.port);
                MicroManager.start();
                SpeakerManager.start();
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§2[" + ModularVoiceChat.MOD_NAME + "] §aSuccessfully established connection with voice-server!"));
            }
            return null;
        }
    }

}
