package fr.nathanael2611.modularvoicechat.network.objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import fr.nathanael2611.modularvoicechat.api.VoiceProperties;

import java.util.HashMap;

public class KryoObjects
{

    public static void registerObjects(Kryo kryo)
    {
        kryo.register(byte[].class);
        kryo.register(byte.class);
        kryo.register(HelloImAPlayer.class);
        kryo.register(VoiceToClient.class);
        kryo.register(VoiceProperties.class);
        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(VoiceToServer.class);
    }

}
