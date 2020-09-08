package fr.nathanael2611.modularvoicechat.server.dispatcher;

import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;

public class GlobalVoiceDispatcher implements IVoiceDispatcher
{

    @Override
    public void dispatch(VoiceDispatchEvent event)
    {
        event.dispatchToAllExceptSpeaker();
    }

}
