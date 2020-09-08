package fr.nathanael2611.modularvoicechat.api.dispatcher;

import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;

public interface IVoiceDispatcher
{

    void dispatch(VoiceDispatchEvent event);

}
