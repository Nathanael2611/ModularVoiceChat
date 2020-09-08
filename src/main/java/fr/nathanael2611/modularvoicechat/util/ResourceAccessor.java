package fr.nathanael2611.modularvoicechat.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

public class ResourceAccessor
{

    private static final String RESOURCE_PATH = "/";

    public static URL getResource(String path)
    {
        return ResourceAccessor.class.getResource(RESOURCE_PATH + path);
    }

    public static InputStream getResourceAsStream(String path)
    {
        return ResourceAccessor.class.getResourceAsStream(RESOURCE_PATH + path);
    }

}
