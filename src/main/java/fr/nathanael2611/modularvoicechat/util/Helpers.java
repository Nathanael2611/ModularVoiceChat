package fr.nathanael2611.modularvoicechat.util;

import com.google.gson.JsonParser;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contain a lot of useful features used in a lot of mod parts
 *
 * @author Nathanael2611
 */
public class Helpers
{
    /**
     * Used for read the content of a file and return a string.
     */
    public static String readFileToString(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "ERROR";
    }

    /**
     * Useful for easily create a List<String> by enter a String Collection in the constructor
     */
    public static List<String> createListFrilStrings(String... str)
    {
        return new ArrayList<>(Arrays.asList(str));
    }

    public static final int EOF = -1;

    public static byte[] toByteArray(final InputStream input) throws IOException
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(final InputStream input, final OutputStream output) throws IOException
    {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE)
        {
            return -1;
        }
        return (int) count;
    }

    public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException
    {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(final InputStream input, final OutputStream output) throws IOException
    {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException
    {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;


    public static boolean isValidJson(String json)
    {
        try
        {
            return new JsonParser().parse(json).getAsJsonObject() != null;
        } catch (Throwable ignored)
        {
        }
        return false;
    }

    public static boolean isOP(String playerName)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!server.isDedicatedServer()) return true;
        for (String oppedPlayerName : server.getPlayerList().getOppedPlayerNames())
        {
            if (oppedPlayerName.equalsIgnoreCase(playerName)) return true;
        }
        return false;
    }

    public static EntityPlayerMP getPlayerMP(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP) return (EntityPlayerMP) player;
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(player.getName());
    }

    public static EntityPlayerMP getPlayerByUsername(String name)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name);
    }

    public static EntityPlayerMP getPlayerByEntityId(int entityId)
    {
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
        {
            if (player.getEntityId() == entityId)
            {
                return player;
            }
        }
        return null;
    }



    public static double getPercent(double value, double max)
    {
        return value * 100 / max;
    }

    public static float getPercent(float value, float max)
    {
        return value * 100 / max;
    }

    public static int getPercent(int value, int max)
    {
        return value * 100 / max;
    }


    private static final Random RANDOM = new Random();

    public static double randomDouble(double min, double max)
    {
        if (min >= max)
        {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return min + (max - min) * RANDOM.nextDouble();
    }

    public static int randomInteger(int min, int max)
    {
        if (min >= max)
        {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static int crossMult(double value, double max, double factor)
    {
        return (int) (value * factor / max);
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static boolean parseOrFalse(String str)
    {
        try
        {
            return Boolean.parseBoolean(str);
        } catch (Exception ex)
        {
            return false;
        }
    }

    public static int parseOrZero(String str)
    {
        try
        {
            return Integer.parseInt(str);
        } catch (Exception ex)
        {
            return 0;
        }
    }

    public static double parseDoubleOrZero(String str)
    {
        try
        {
            return Double.parseDouble(str);
        } catch (Exception ex)
        {
            return 0;
        }
    }

    public static String[] getStringListAsArray(List<String> list)
    {
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            array[i] = list.get(i);
        }
        return array;
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm");

    public static void log(String string)
    {
        System.out.println(String.format(("[%s] [%s] " + string), DATE_FORMAT.format(new Date()), ModularVoiceChat.MOD_NAME));
    }

}
