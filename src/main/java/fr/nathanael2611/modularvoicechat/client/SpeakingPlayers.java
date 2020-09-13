package fr.nathanael2611.modularvoicechat.client;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class SpeakingPlayers
{

    private static HashMap<Integer, Long> SPEAKING_PLAYERS = Maps.newHashMap();

    public static boolean isTalking(EntityPlayer player)
    {
        return System.currentTimeMillis() - getTalkingValue(player) < 200;
    }

    public static void updateTalking(int id)
    {
        SPEAKING_PLAYERS.put(id, System.currentTimeMillis());
    }

    private static long getTalkingValue(EntityPlayer player)
    {
        return SPEAKING_PLAYERS.getOrDefault(player.getEntityId(), 0L);
    }


}
