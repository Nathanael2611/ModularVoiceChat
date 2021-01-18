package fr.nathanael2611.modularvoicechat.config;

import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.server.dispatcher.DistanceBasedVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.server.dispatcher.GlobalVoiceDispatcher;
import net.minecraftforge.common.config.Config;

@Config(modid = ModularVoiceChat.MOD_ID, name = ModularVoiceChat.MOD_NAME + "/ServerConfig")
public class ServerConfig
{

    @Config.Comment({"This is the general config of ModularVoiceChat"})
    public static General generalConfig = new General();

    public static class General
    {
        @Config.Comment("The vocal-server port")
        public int port = ModularVoiceChat.DEFAULT_PORT;

        @Config.Comment("Define if the micro on the speaking-players will be rendered.")
        public boolean showWhoSpeak = false;

        @Config.Comment("The used voice-dispatcher")
        public Dispatcher dispatcher = new Dispatcher();

        @Config.Comment({"This field is optionnal, but may correct some issue with connecting to voice-server!", "By providing an given hostname you are assured that all players use the same."})
        public String forcedHostname = "";
    }

    public static class Dispatcher
    {
        @Config.Comment({"The DispatchType", " - \"distanced\" for a distance-based voice-dispatch", " - \"global\" for a global, to all players, voice-dispatch"})
        public String dispatchType = "distanced";
        @Config.Comment({"If DispatchType is \"distanced\", it will be the max-distance that a player can hear another one."})
        public int maxDistance = 15;
        @Config.Comment({"If DispatchType is \"distanced\":", "If true, the sound will fade-out with the distance"})
        public boolean fadeOut = true;

        public IVoiceDispatcher createDispatcher()
        {
            if(this.dispatchType.equalsIgnoreCase("global"))
            {
                return new GlobalVoiceDispatcher();
            }
            else
            {
                return new DistanceBasedVoiceDispatcher(this.maxDistance, this.fadeOut);
            }
        }
    }

}
