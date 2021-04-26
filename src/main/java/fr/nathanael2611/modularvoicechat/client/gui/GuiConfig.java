package fr.nathanael2611.modularvoicechat.client.gui;

import com.google.gson.JsonPrimitive;
import net.minecraft.client.resources.I18n;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.audio.AudioTester;
import fr.nathanael2611.modularvoicechat.client.ClientEventHandler;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import fr.nathanael2611.modularvoicechat.audio.micro.MicroData;
import fr.nathanael2611.modularvoicechat.audio.speaker.SpeakerData;
import fr.nathanael2611.modularvoicechat.util.AudioUtil;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class GuiConfig extends GuiScreen
{

    public static boolean audioTesting = false;

    private ClientConfig config;

    public GuiConfig()
    {
        this.config = ClientProxy.getConfig();
    }

    private GuiDropDownMenu microSelector;
    private GuiDropDownMenu speakerSelector;
    private GuiConfigSlider microVolume;
    private GuiConfigSlider speakerVolume;
    private GuiButton toggleToTalk;
    private GuiButton audioTest;
    private GuiButton stereo;

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.clear();
        audioTesting = false;
        AudioTester.updateTester();
        int y = 80 + 20;
        this.buttonList.add(this.microVolume = new GuiConfigSlider(this, 12, width / 2 - 150 - 5, y + 25, ClientConfig.MICROPHONE_VOLUME, 0, 150));
        this.buttonList.add(this.speakerVolume = new GuiConfigSlider(this, 12, width/ 2 + 5, y + 25, ClientConfig.SPEAKER_VOLUME, 0, 150));
        this.buttonList.add(this.toggleToTalk = new GuiButton(13, width / 2 - 150 - 5, y + 50, 150, 20, "Mode: " + getSpeakMode()));
        this.buttonList.add(this.audioTest = new GuiButton(14, width / 2 + 5, y + 50, 150, 20, (audioTesting ? I18n.format("mvc.config.audio.test.on") : I18n.format("mvc.config.audio.test.off"))));
        this.buttonList.add(new GuiButton(1, width / 2 - 155, y + 50 + 25 + 25,150 + 5 + 5 + 150, 20, I18n.format("mvc.config.joindiscord")));
        this.buttonList.add(this.stereo = new GuiButton(2, width / 2 - 155, y + 50 + 25,150 + 5 + 5 + 150, 20, getStereoMode()));
        this.buttonList.add(this.microSelector = new GuiDropDownMenu(12, width / 2 - 150 - 4, y, 148, 20, MicroManager.getHandler().getMicro(), Helpers.getStringListAsArray(AudioUtil.findAudioDevices(MicroData.MIC_INFO))));
        this.buttonList.add(this.speakerSelector = new GuiDropDownMenu(13, width / 2 + 6, y, 148, 20, SpeakerManager.getHandler().getSpeaker(), Helpers.getStringListAsArray(AudioUtil.findAudioDevices(SpeakerData.SPEAKER_INFO))));
    }

    public String getSpeakMode()
    {
        return this.config.get(ClientConfig.TOGGLE_TO_TALK).getAsBoolean() ? I18n.format("mvc.config.toggletotalk") : I18n.format("mvc.config.pushtotalk");
    }

    public String getStereoMode()
    {
        return this.config.isStereo() ? I18n.format("mvc.config.stereo.on") : I18n.format("mvc.config.stereo.off");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);
        GlStateManager.translate((width / 2) - 32 - (fontRenderer.getStringWidth(ModularVoiceChat.MOD_NAME) / 2), 30, 1);
        GlStateManager.scale(1.2, 1.2, 1.2);
        mc.getTextureManager().bindTexture(ClientEventHandler.MICRO);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 32, 32, 32, 32);
        fontRenderer.drawStringWithShadow(ModularVoiceChat.MOD_NAME, 32, 10, Color.WHITE.getRGB());
        GlStateManager.popMatrix();
        mc.fontRenderer.drawStringWithShadow("§n" + I18n.format("mvc.config.audio.input"), width / 2 - 150 - 5, 80, Color.WHITE.getRGB());
        mc.fontRenderer.drawStringWithShadow("§n" + I18n.format("mvc.config.audio.output"), width / 2 + 5, 80, Color.WHITE.getRGB());
        mc.fontRenderer.drawStringWithShadow("§7" + I18n.format("mvc.config.canMute"), width / 2 - 150 - 5, 200, Color.WHITE.getRGB());


        super.drawScreen(mouseX, mouseY, partialTicks);
        if(mouseX > this.microSelector.x && mouseX < this.microSelector.x + this.microSelector.width &&
                mouseY > this.microSelector.y && mouseY < this.microSelector.y + this.microSelector.height &&
                !this.microSelector.dropDownMenu)
        {
            this.drawHoveringText(I18n.format("mvc.config.audio.output.desc"), mouseX, mouseY);
        }
        else if(mouseX > this.speakerSelector.x && mouseX < this.speakerSelector.x + this.speakerSelector.width &&
                mouseY > this.speakerSelector.y && mouseY < this.speakerSelector.y + this.speakerSelector.height &&
                !this.speakerSelector.dropDownMenu)
        {
            this.drawHoveringText(I18n.format("mvc.config.audio.input.desc"), mouseX, mouseY);
        }
        else if(mouseX > this.microVolume.x && mouseX < this.microVolume.x + this.microVolume.width &&
                mouseY > this.microVolume.y && mouseY < this.microVolume.y + this.microVolume.height)
        {
            this.drawHoveringText(I18n.format("mvc.config.audio.input.volume"), mouseX, mouseY);
        }
        else if(mouseX > this.speakerVolume.x && mouseX < this.speakerVolume.x + this.speakerVolume.width &&
                mouseY > this.speakerVolume.y && mouseY < this.speakerVolume.y + this.speakerVolume.height)
        {
            this.drawHoveringText(I18n.format("mvc.config.audio.output.volume"), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if(button instanceof GuiDropDownMenu)
        {
            GuiDropDownMenu drop = (GuiDropDownMenu) button;
            if(drop == this.microSelector)
            {
                MicroManager.getHandler().setMicro(this.microSelector.getSelectedText());
            }
            else
            {
                SpeakerManager.getHandler().setSpeaker(this.speakerSelector.getSelectedText());

            }
            if(drop.dropDownMenu)
            {
                drop.displayString = drop.getSelectedText();
            }
            drop.dropDownMenu = !drop.dropDownMenu;
        }
        else if(!this.speakerSelector.dropDownMenu && !this.microSelector.dropDownMenu)
        {
            if(button == this.toggleToTalk)
            {
                this.config.set(ClientConfig.TOGGLE_TO_TALK, new JsonPrimitive(!this.config.get(ClientConfig.TOGGLE_TO_TALK).getAsBoolean()));
                button.displayString = "Mode: " + getSpeakMode();
            }
            if(button == this.stereo)
            {
                this.config.set(ClientConfig.STEREO, new JsonPrimitive(!this.config.isStereo()));
                button.displayString = getStereoMode();
            }
            if(button == this.audioTest)
            {
                audioTesting = !audioTesting;

                button.displayString = (audioTesting ? I18n.format("mvc.config.audio.test.on") : I18n.format("mvc.config.audio.test.off"));
                AudioTester.updateTester();
            }
            else if(button.id == 1 )
            {
                Desktop.getDesktop().browse(URI.create(ModularVoiceChat.DISCORD_INVITE));
            }
        }
    }

    public boolean canChangeVolume()
    {
        return !(this.microSelector.isMouseOver() || this.speakerSelector.isMouseOver());
    }

    @Override
    public void onGuiClosed()
    {
        audioTesting = false;
        AudioTester.updateTester();

    }
}
