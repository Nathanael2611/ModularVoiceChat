package fr.nathanael2611.modularvoicechat.network.objects;

import com.esotericsoftware.kryo.Kryo;

public class KryoObjects
{

    public static void registerObjects(Kryo kryo)
    {
        kryo.register(byte[].class);
        kryo.register(byte.class);
        kryo.register(HelloImAPlayer.class);
        kryo.register(VoiceToClient.class);
        kryo.register(VoiceToServer.class);
    }

}
