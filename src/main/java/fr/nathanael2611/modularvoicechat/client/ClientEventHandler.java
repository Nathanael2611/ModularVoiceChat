package fr.nathanael2611.modularvoicechat.client;

import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import fr.nathanael2611.modularvoicechat.client.gui.GuiConfig;
import fr.nathanael2611.modularvoicechat.client.voice.VoiceClientManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.MicroManager;
import fr.nathanael2611.modularvoicechat.client.voice.audio.SpeakerManager;
import fr.nathanael2611.modularvoicechat.config.ClientConfig;
import fr.nathanael2611.modularvoicechat.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

/**
 * This will contain all events related to client.
 */
public class ClientEventHandler
{

    /**
     * Micro texture
     */
    public static final ResourceLocation MICRO = new ResourceLocation(ModularVoiceChat.MOD_ID, "textures/micro.png");

    public static boolean showWhoSpeak = false;

    /* Simply store the Minecraft instance */
    private Minecraft mc;

    public ClientEventHandler(Minecraft minecraft)
    {
        this.mc = minecraft;
    }

    /**
     * Used to add the config button to GuiInGame
     */
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent event)
    {
        if (event.getGui() instanceof GuiIngameMenu && MicroManager.isRunning() && SpeakerManager.isRunning())
        {
            event.getButtonList().add(new GuiButton(434, (event.getGui().width / 2) - 100, 0, ModularVoiceChat.MOD_NAME));
        }
    }

    /**
     * Used to open the Config Gui from the button
     */
    @SubscribeEvent
    public void onGuiActionPerformed(GuiScreenEvent.ActionPerformedEvent event)
    {
        if (event.getGui() instanceof GuiIngameMenu && event.getButton().id == 434)
        {
            this.mc.displayGuiScreen(new GuiConfig());
        }
    }

    /* Alpha value, used for draw micro */
    private float alpha = 0.3f;

    /**
     * Used for render things on overlay.
     */
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            ScaledResolution resolution = event.getResolution();
            if (VoiceClientManager.isStarted() && MicroManager.isRunning() && VoiceClientManager.getClient().isConnected())
            {
                if (!GuiConfig.audioTesting)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(resolution.getScaledWidth() - 32, resolution.getScaledHeight() - 32, 0);
                    this.alpha = Math.max(0f, Math.min(1, MicroManager.getHandler().isSending() ? this.alpha + 0.1f : this.alpha - 0.05f));
                    GlStateManager.color(1, 1, 1, alpha);
                    GlStateManager.scale(0.8, 0.8, 0.8);
                    mc.getTextureManager().bindTexture(MICRO);
                    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 32, 32, 32, 32);
                    GlStateManager.popMatrix();
                }
            } else
            {
                mc.fontRenderer.drawStringWithShadow(String.format("§c[%s] %s", ModularVoiceChat.MOD_NAME, I18n.format("mvc.error.notconnected")), 2, 2, Color.WHITE.getRGB());
            }
        }
        GlStateManager.color(1, 1, 1, 1F);
    }

    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Post event)
    {
        if (mc.gameSettings.hideGUI || !showWhoSpeak) return;
        boolean flag = SpeakingPlayers.isTalking(event.getEntityPlayer());
        if (flag)
        {
            final float factor = 0.01f;
            double scale = 1.5;
            GlStateManager.pushMatrix();
            GlStateManager.translate(event.getX(), event.getY() + ((event.getEntityPlayer().isSneaking() ? 2.4 : 2.5)), event.getZ());
            GlStateManager.glNormal3f(1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.scale(-factor * scale, -factor * scale, -factor * scale);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0F, 0.0f);
            mc.getTextureManager().bindTexture(MICRO);
            Gui.drawModalRectWithCustomSizedTexture(-16, -16, 0, 0, 32, 32, 32, 32);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

    }

    /**
     * Used for handle different things. (Key, stopping, etc)
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (mc.player == null && mc.world == null)
            {
                if(SpeakerManager.isRunning())
                {
                    SpeakerManager.stop();
                }
                if(MicroManager.isRunning())
                {
                    MicroManager.stop();
                }
                if(VoiceClientManager.isStarted())
                {
                    VoiceClientManager.stop();
                }
            }
            if (ClientProxy.KEY_OPEN_CONFIG.isPressed() && MicroManager.isRunning() && SpeakerManager.isRunning())
            {
                this.mc.displayGuiScreen(new GuiConfig());
            } else if (!GuiConfig.audioTesting)
            {
                if (ClientProxy.getConfig().get(ClientConfig.TOGGLE_TO_TALK).getAsBoolean())
                {
                    if (ClientProxy.KEY_SPEAK.isPressed())
                    {
                        if (MicroManager.isRunning() && !MicroManager.getHandler().isSending())
                        {
                            MicroManager.getHandler().start();
                        } else
                        {
                            if (MicroManager.isRunning() && MicroManager.getHandler().isSending())
                            {
                                MicroManager.getHandler().stop();
                            }
                        }
                    }

                } else if (GameSettings.isKeyDown(ClientProxy.KEY_SPEAK))
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

}
