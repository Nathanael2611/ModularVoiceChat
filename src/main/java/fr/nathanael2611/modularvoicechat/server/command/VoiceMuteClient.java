package fr.nathanael2611.modularvoicechat.server.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.modularvoicechat.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class VoiceMuteClient extends VoiceMute
{

    @Override
    public String getName()
    {
        return "local" + super.getName();
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "mute", "unmute", "list");
        }
        else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("mute"))
            {
                List<String> playerNames = Lists.newArrayList();
                for (NetworkPlayerInfo networkPlayerInfo : Minecraft.getMinecraft().getConnection().getPlayerInfoMap())
                {
                    playerNames.add(networkPlayerInfo.getGameProfile().getName());
                }
                return getListOfStringsMatchingLastWord(args, playerNames);
            }
            else if(args[0].equalsIgnoreCase("unmute"))
            {
                return getListOfStringsMatchingLastWord(args, CommonProxy.getMutedPlayers().getNames());
            }
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}
