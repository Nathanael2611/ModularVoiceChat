package fr.nathanael2611.modularvoicechat.api.dispatcher;

import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;

/**
 * Represent a Voice-Dispatcher
 */
public interface IVoiceDispatcher
{

    /**
     * Used for dispatching all voices
     * @param event the VoiceDispatchEvent to use
     */
    void dispatch(VoiceDispatchEvent event);

}
