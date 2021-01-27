package fr.nathanael2611.modularvoicechat.client.gui;

import com.google.gson.JsonPrimitive;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.config.ConfigProperty;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import fr.nathanael2611.modularvoicechat.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiConfigSlider extends GuiButton
{

    private GuiConfig parent;
    private float sliderValue;
    public boolean dragging;
    private final ConfigProperty property;
    private final float minValue;
    private final float maxValue;

    public GuiConfigSlider(GuiConfig parent, int buttonId, int x, int y, ConfigProperty property, float minValueIn, float maxValue)
    {
        super(buttonId, x, y, 150, 20, "");
        this.parent = parent;
        this.sliderValue = 1.0F;
        this.property = property;
        this.minValue = minValueIn;
        this.maxValue = maxValue;
        this.sliderValue = (
                minValue + ClientProxy.getConfig().get(property).getAsInt()
                        * 1 / maxValue);
        displayString = "Volume: " + ClientProxy.getConfig().get(property).getAsInt() + "%";

    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        this.enabled = true;
        super.drawButton(mc, mouseX, mouseY, partialTicks);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible && parent.canChangeVolume())
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
                float f = sliderValue;
                //ClientProxy.getConfig().set(this.property, new JsonPrimitive((int) this.sliderValue));

                this.sliderValue = sliderValue;

                int val = (int) (minValue + Helpers.crossMult(sliderValue, 1, maxValue));
                displayString =  val + "%";

                //this.displayString = mc.gameSettings.getKeyBinding(this.options);
            }

            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.parent.canChangeVolume() && super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }



    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
        if(!this.parent.canChangeVolume())
        {
            return;
        }
        int val = (int) (minValue + Helpers.crossMult(sliderValue, 1, maxValue));
        if(property == ClientConfig.MICROPHONE_VOLUME)
        {
            MicroManager.getHandler().setVolume(val);
        }
        else if(property == ClientConfig.SPEAKER_VOLUME)
        {
            SpeakerManager.getHandler().setVolume(val);
        }
        else
        {
            ClientProxy.getConfig().set(this.property, new JsonPrimitive(val));
        }
    }
}