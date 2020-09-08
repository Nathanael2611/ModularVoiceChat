package fr.nathanael2611.modularvoicechat.util;

import com.sun.jna.Platform;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Objects;

public class OpusLoader
{

    public static boolean loadOpus()
    {
        try
        {
            loadNativesFromJar();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static void loadNativesFromJar() throws Exception
    {
        String property = System.getProperty("os.name", "bare-metal?").toLowerCase();
        File systemTmp = new File(System.getProperty("java.io.tmpdir"));
        File tmp = new File(systemTmp, String.format("ModularVoiceChat-opus-%s", System.currentTimeMillis() / 1000) + property.replace(' ', '-'));
        FileUtils.deleteDirectory(tmp);
        tmp.mkdirs();
        String path = System.getProperty("java.library.path");
        System.setProperty("java.library.path", path + (path != null && !path.isEmpty() ? ";" : "") + tmp.getAbsolutePath());
        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);
        if (!property.contains("nux") && !property.contains("nix"))
        {
            if (property.contains("mac"))
            {
                Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/mac/libopus_jni.dylib")), (new File(tmp, "libopus_jni.dylib")).toPath(), new CopyOption[0]);
                System.loadLibrary("libopus_jni");
            } else if (property.contains("windows"))
            {
                Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/windows/libopus-0.dll")), (new File(tmp, "libopus-0.dll")).toPath(), new CopyOption[0]);
                Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/windows/libopus-0_jni.dll")), (new File(tmp, "libopus-0_jni.dll")).toPath(), new CopyOption[0]);
                System.loadLibrary("libopus-0");
                System.loadLibrary("libopus-0_jni");
            }
        } else
        {
            Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/linux/libopus.so")), (new File(tmp, "libopus.so")).toPath(), new CopyOption[0]);
            Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/linux/libopus_jni.so")), (new File(tmp, "libopus_jni.so")).toPath(), new CopyOption[0]);
            System.loadLibrary("libopus");
            System.loadLibrary("libopus_jni");
        }

    }

}
