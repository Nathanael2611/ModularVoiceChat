package fr.nathanael2611.modularvoicechat.proxy;

import fr.nathanael2611.modularvoicechat.server.ServerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy
{

    @Override
    public void onPreInitialization(FMLPreInitializationEvent event)
    {
        super.onPreInitialization(event);


        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);
    }

    @Override
    public void onPostInitialization(FMLPostInitializationEvent event)
    {
        super.onPostInitialization(event);
    }
}
