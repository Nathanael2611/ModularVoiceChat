package fr.nathanael2611.modularvoicechat.api;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event used to able the modders to disable the default keybinding of modularvoicechat in certain situations.
 * And for disable or not the toggle to talk in certain situations
 */
public class VoiceKeyEvent extends Event
{

    private boolean toggleToTalk;

    public VoiceKeyEvent(boolean toggleToTalk)
    {
        this.toggleToTalk = toggleToTalk;
    }

    public boolean isToggleToTalk()
    {
        return toggleToTalk;
    }

    public void setToggleToTalk(boolean toggleToTalk)
    {
        this.toggleToTalk = toggleToTalk;
    }

    @Override
    public boolean isCancelable()
    {
        return true;
    }
}
