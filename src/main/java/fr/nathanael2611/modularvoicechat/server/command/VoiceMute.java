package fr.nathanael2611.modularvoicechat.server.command;

import fr.nathanael2611.modularvoicechat.proxy.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class VoiceMute extends CommandBase
{
    @Override
    public String getName()
    {
        return "voicemute";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "voicemute <mute/unmute/list> [<player>]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(new TextComponentTranslation("mvc.moderation.mute.mutedPlayers"));
                for (String name : CommonProxy.getMutedPlayers().getNames())
                {
                    sender.sendMessage(new TextComponentString("§8 - §7" + name));
                }
                return;
            }
            else if(args.length > 1)
            {
                if(args[0].equalsIgnoreCase("mute"))
                {
                    CommonProxy.getMutedPlayers().setMute(args[1], true);
                    sender.sendMessage(new TextComponentTranslation("mvc.moderation.mute.playerMuted", args[1]));
                    return;
                }
                else if(args[0].equalsIgnoreCase("unmute"))
                {
                    CommonProxy.getMutedPlayers().setMute(args[1], false);
                    sender.sendMessage(new TextComponentTranslation("mvc.moderation.mute.playerUnmuted", args[1]));
                    return;
                }
            }
        }
        throw new WrongUsageException(getUsage(sender));
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
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            }
            else if(args[0].equalsIgnoreCase("unmute"))
            {
                return getListOfStringsMatchingLastWord(args, CommonProxy.getMutedPlayers().getNames());
            }
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
