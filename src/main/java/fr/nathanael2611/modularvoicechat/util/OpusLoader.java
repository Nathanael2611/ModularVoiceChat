package fr.nathanael2611.modularvoicechat.util;

import com.sun.jna.Platform;
import fr.nathanael2611.modularvoicechat.ModularVoiceChat;
import net.labymod.opus.OpusCodec;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class OpusLoader
{

    public static boolean loadOpus()
    {
        String property = System.getProperty("os.name", "bare-metal?").toLowerCase();
        try
        {
            loadNatives();
            return true;
        } catch (Throwable e)
        {
            e.printStackTrace();
            return false;
        }
    }


    private static void loadNatives() throws Throwable {
        final String tmpDir = System.getProperty("java.io.tmpdir");
        File tempLibDir = null;
        int index = 0;
        do {
            tempLibDir = new File(tmpDir, "modularvoicechatopus" + index);
            ++index;
        } while (tempLibDir.exists() && !deleteDir(tempLibDir));
        tempLibDir.mkdir();
        OpusCodec.extractNatives(tempLibDir);
        OpusCodec.loadNative(tempLibDir);
    }


    private static boolean deleteDir(final File file) {
        final File[] contents = file.listFiles();
        if (contents != null) {
            File[] array;
            for (int length = (array = contents).length, i = 0; i < length; ++i) {
                final File f = array[i];
                deleteDir(f);
            }
        }
        return file.delete();
    }

   /* private static void loadNativesFromJar() throws Exception
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
                File file = new File(tmp, "libopus-0.dylib");
                Files.copy(Objects.requireNonNull(OpusCodec.class.getResourceAsStream("/native/64/mac/libopus-0.dylib"), "La lib opus n'a pas été trouvée dans le jar"), (file).toPath(), new CopyOption[0]);
                File p = (new File(tmp, "libopus_jni.dylib"));
                Files.copy(Objects.requireNonNull(OpusCodec.class.getResourceAsStream("/native/64/mac/libopus_jni.dylib"), "La lib opus n'a pas été trouvée dans le jar"), p.toPath(), new CopyOption[0]);
                System.load(file.getAbsolutePath());
                System.load(p.getAbsolutePath());
            } else if (property.contains("windows"))
            {
                Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/windows/libopus-0.dll"), "La lib opus n'a pas été trouvée dans le jar"), (new File(tmp, "libopus-0.dll")).toPath(), new CopyOption[0]);
                Files.copy(Objects.requireNonNull(ResourceAccessor.getResourceAsStream("native/64/windows/libopus-0_jni.dll"), "La lib opus n'a pas été trouvée dans le jar"), (new File(tmp, "libopus-0_jni.dll")).toPath(), new CopyOption[0]);
                System.loadLibrary("libopus-0");
                System.loadLibrary("libopus-0_jni");
            }
        } else
        {
            File file = new File(tmp, "libopus.so");
            Files.copy(Objects.requireNonNull(OpusCodec.class.getResourceAsStream("/native/64/linux/libopus.so"), "La lib opus n'a pas été trouvée dans le jar"), (file).toPath(), new CopyOption[0]);
            File fileJni = new File(tmp, "libopus_jni.so");
            Files.copy(Objects.requireNonNull(OpusCodec.class.getResourceAsStream("/native/64/linux/libopus_jni.so"), "La lib opus n'a pas été trouvée dans le jar"), (fileJni).toPath(), new CopyOption[0]);
            System.load(file.getAbsolutePath());
            System.load(fileJni.getAbsolutePath());
        }

    }*/

    public static void main(String[] args)
    {
        System.out.println("Start :eyes:");
        try
        {
            System.out.println("On test...");
            loadNatives();
            System.out.println("Réussite! Haha!");
        } catch (Throwable e)
        {
            e.printStackTrace();
            System.out.println("Ca n'a pas fonctionné :c");

        }
        System.out.println("End :eyes:");
    }

}
