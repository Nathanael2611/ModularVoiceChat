package fr.nathanael2611.modularvoicechat.client;

import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.client.gui.GuiConfig;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ClientEventHandler
{

    public static final ResourceLocation MICRO = new ResourceLocation(ModularVoiceChat.MOD_ID, "textures/micro.png");

    private Minecraft mc;

    public ClientEventHandler(Minecraft minecraft)
    {
        this.mc = minecraft;
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent event)
    {
        if(event.getGui() instanceof GuiIngameMenu && MicroManager.isRunning() && SpeakerManager.isRunning())
        {
            event.getButtonList().add(new GuiButton(434, (event.getGui().width / 2) - 100, 0, ModularVoiceChat.MOD_NAME));
        }
    }

    @SubscribeEvent
    public void onGuiActionPerformed(GuiScreenEvent.ActionPerformedEvent event)
    {
        if(event.getGui() instanceof GuiIngameMenu && event.getButton().id == 434)
        {
            this.mc.displayGuiScreen(new GuiConfig());
        }
    }

    private float alpha = 0.3f;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
    {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            ScaledResolution resolution = event.getResolution();
            if(MicroManager.isRunning())
            {
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(resolution.getScaledWidth() - 32, resolution.getScaledHeight() - 32, 0);
                    //GlStateManager.translate(event.getPartialTicks() * 0.5, 0, 0);
                    this.alpha = Math.max(0f, Math.min(1, MicroManager.getHandler().isSending() ? this.alpha + 0.1f : this.alpha - 0.05f));
                    GlStateManager.color(1, 1, 1, alpha);
                    GlStateManager.scale(0.8, 0.8, 0.8);
                    mc.getTextureManager().bindTexture(MICRO);
                    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 32, 32, 32, 32);
                    GlStateManager.popMatrix();

                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if(ClientProxy.KEY_OPEN_CONFIG.isPressed() && MicroManager.isRunning() && SpeakerManager.isRunning())
            {
                this.mc.displayGuiScreen(new GuiConfig());
            }
            else if (Keyboard.isKeyDown(ClientProxy.KEY_SPEAK.getKeyCode()))
            {
                if (MicroManager.isRunning() && !MicroManager.getHandler().isSending())
                {
                    MicroManager.getHandler().start();
                }
            } else
            {
                if (MicroManager.isRunning() && MicroManager.getHandler().isSending())
                {
                    MicroManager.getHandler().stop();
                }
            }
        }
    }

}
