package fr.nathanael2611.modularvoicechat.api;

import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.server.VoiceServer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class VoiceServerStartEvent extends Event
{

    private VoiceServer server;
    private IVoiceDispatcher voiceDispatcher;

    public VoiceServerStartEvent(VoiceServer server, IVoiceDispatcher voiceDispatcher)
    {
        this.server = server;
        this.voiceDispatcher = voiceDispatcher;
    }

    public VoiceServer getServer()
    {
        return server;
    }

    public void changeVoiceDispatcher(IVoiceDispatcher newVoiceDispatcher)
    {
        this.voiceDispatcher = newVoiceDispatcher;
    }

    public IVoiceDispatcher getVoiceDispatcher()
    {
        return voiceDispatcher;
    }

}
